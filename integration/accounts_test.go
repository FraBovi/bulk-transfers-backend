//go:build integration

package integration_test

import (
	"context"
	"encoding/json"
	"net/http"
	"os"

	"github.com/docker/docker/errdefs"
	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
	"github.com/ossan-dev/bulktransfertests/helpers"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
	_ "gorm.io/driver/mysql"
)

var _ = Describe("accounts", Ordered, func() {
	BeforeAll(func() {
		os.Setenv("TESTCONTAINERS_RYUK_DISABLED", "true")
		composeReq, err := tc.NewDockerComposeWith(tc.WithStackFiles("../docker-compose.yml"))
		Expect(err).Should(BeNil())
		DeferCleanup(func() {
			Expect(composeReq.Down(context.Background(), tc.RemoveOrphans(true), tc.RemoveImagesLocal)).Should(BeNil())
		})
		ctx, cancel := context.WithCancel(context.Background())
		DeferCleanup(cancel)
		var composeErr error
		composeErr = composeReq.
			WaitForService("api_service", wait.ForListeningPort("8080/tcp")).
			Up(ctx, tc.Wait(true))
		if composeErr != nil && errdefs.IsConflict(composeErr) {
			composeErr = helpers.ReRunContainersAfterConflict(ctx, composeReq)
		}
		Expect(composeErr).Should(BeNil())
	})

	Describe("retrieving accounts", func() {
		Context("HTTP request is valid", func() {
			It("return accounts if present in DB", func() {
				client := http.Client{}
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?iban=IT10474608000005006107XXXXX", nil)
				res, err := client.Do(r)
				Expect(err).Should(BeNil())
				Expect(res).To(HaveHTTPStatus(http.StatusOK))
				var accounts []helpers.Account
				Expect(json.NewDecoder(res.Body).Decode(&accounts)).Should(BeNil())
				DeferCleanup(res.Body.Close)
				Expect(accounts).To(HaveLen(1))
				Expect(accounts[0].Iban).To(Equal("IT10474608000005006107XXXXX"))
			})
			It("do not return if not present in DB", func() {
				client := http.Client{}
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?name=notexistentcompany", nil)
				Expect(err).Should(BeNil())
				res, err := client.Do(r)
				Expect(err).Should(BeNil())
				Expect(res).To(HaveHTTPStatus(http.StatusOK))
				Expect(res).To(HaveHTTPBody("[]"))
			})
		})

		Context("HTTP is not valid", func() {
			It("err with invalid IBAN", func() {
				client := http.Client{}
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?iban=abcd", nil)
				Expect(err).Should(BeNil())
				res, err := client.Do(r)
				Expect(err).Should(BeNil())
				Expect(res).To(HaveHTTPStatus(http.StatusBadRequest))
			})

			It("err with empty name", func() {
				client := http.Client{}
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?name=", nil)
				Expect(err).Should(BeNil())
				res, err := client.Do(r)
				Expect(err).Should(BeNil())
				Expect(res).To(HaveHTTPStatus(http.StatusBadRequest))
			})
		})
	})
})
