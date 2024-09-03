package integration_test

import (
	"context"
	"database/sql"
	"encoding/json"
	"net/http"
	"os"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
	_ "gorm.io/driver/mysql"
)

var _ = Describe("integration tests accounts", Ordered, func() {
	BeforeAll(func() {
		os.Setenv("TESTCONTAINERS_RYUK_DISABLED", "true")
		compose, err := tc.NewDockerComposeWith(tc.WithStackFiles("../docker-compose.yml"))
		Expect(err).Should(BeNil())
		DeferCleanup(func() {
			Expect(compose.Down(context.Background(), tc.RemoveOrphans(true), tc.RemoveImagesLocal)).Should(BeNil())
		})
		ctx, cancel := context.WithCancel(context.Background())
		DeferCleanup(cancel)
		err = compose.
			WaitForService("api_service", wait.ForListeningPort("8080/tcp")).
			Up(ctx, tc.Wait(true))
		Expect(err).Should(BeNil())
		sqlClient, err := sql.Open("mysql", "root:root@tcp(127.0.0.1:3307)/transfers_db")
		Expect(err).Should(BeNil())
		DeferCleanup(func() {
			Expect(sqlClient.Close()).Should(BeNil())
		})
		Expect(sqlClient.Ping()).Should(BeNil())
	})

	When("HTTP request is valid", func() {
		It("return accounts if present in DB", func() {
			client := http.Client{}
			r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?iban=IT10474608000005006107XXXXX", nil)
			Expect(err).Should(BeNil())
			res, err := client.Do(r)
			Expect(err).Should(BeNil())
			Expect(res).To(HaveHTTPStatus(http.StatusOK))
			var accounts []account
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
})
