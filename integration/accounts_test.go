//go:build integration

package integration_test

import (
	"encoding/json"
	"net/http"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
	"github.com/ossan-dev/bulktransfertests/helpers"
	_ "gorm.io/driver/mysql"
)

var _ = Describe("accounts endpoint", Ordered, func() {
	Describe("retrieving accounts", func() {
		Context("HTTP request is valid", func() {
			It("return accounts if present in DB", func() {
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?iban=IT10474608000005006107XXXXX", nil)
				Expect(err).ShouldNot(HaveOccurred())
				res, err := httpClient.Do(r)
				Expect(err).ShouldNot(HaveOccurred())
				Expect(res).To(HaveHTTPStatus(http.StatusOK))
				var accounts []helpers.Account
				Expect(json.NewDecoder(res.Body).Decode(&accounts)).Should(BeNil())
				DeferCleanup(res.Body.Close)
				Expect(accounts).To(HaveLen(1))
				Expect(accounts[0].Iban).To(Equal("IT10474608000005006107XXXXX"))
			})
			It("do not return if not present in DB", func() {
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?name=notexistentcompany", nil)
				Expect(err).ShouldNot(HaveOccurred())
				res, err := httpClient.Do(r)
				Expect(err).ShouldNot(HaveOccurred())
				Expect(res).To(HaveHTTPStatus(http.StatusOK))
				Expect(res).To(HaveHTTPBody("[]"))
			})
		})

		Context("HTTP is not valid", func() {
			It("err with invalid IBAN", func() {
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?iban=abcd", nil)
				Expect(err).ShouldNot(HaveOccurred())
				res, err := httpClient.Do(r)
				Expect(err).ShouldNot(HaveOccurred())
				Expect(res).To(HaveHTTPStatus(http.StatusBadRequest))
			})

			It("err with empty name", func() {
				r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts?name=", nil)
				Expect(err).ShouldNot(HaveOccurred())
				res, err := httpClient.Do(r)
				Expect(err).ShouldNot(HaveOccurred())
				Expect(res).To(HaveHTTPStatus(http.StatusBadRequest))
			})
		})
	})
})
