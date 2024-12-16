//go:build integration

package integration_test

import (
	"bytes"
	_ "embed"
	"encoding/json"
	"net/http"
	"strconv"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
)

//go:embed testdata/single_request.json
var requestBytes []byte

var _ = Describe("transfers endpoint", Ordered, func() {
	var getAccountBalanceByCompanyName func(companyName string) (balanceCents *int64)

	BeforeAll(func() {
		getAccountBalanceByCompanyName = func(companyName string) (balanceCents *int64) {
			row := sqlClient.QueryRowContext(ctx, "select balance_cents from bank_accounts ba where organization_name = ?;", companyName)
			Expect(row.Err()).ShouldNot(HaveOccurred())
			err := row.Scan(&balanceCents)
			Expect(err).ShouldNot(HaveOccurred())
			return
		}
	})

	Describe("valid transfers", func() {
		Context("receive one valid transfer", func() {
			It("updates the amount_cents correctly", func() {
				originalBalanceCents := getAccountBalanceByCompanyName("COMPANY 1")
				Expect(originalBalanceCents).ShouldNot(BeNil())
				r, err := http.NewRequestWithContext(ctx, http.MethodPost, "http://localhost:8080/api/customer/transfers", bytes.NewReader(requestBytes))
				r.Header.Set("Content-Type", "application/json")
				Expect(err).ShouldNot(HaveOccurred())
				Expect(r).ShouldNot(BeNil())
				res, err := httpClient.Do(r)
				Expect(err).ShouldNot(HaveOccurred())
				defer res.Body.Close()

				var amountField struct {
					CreditTransfers []struct {
						Amount string `json:"amount"`
					} `json:"credit_transfers"`
				}

				err = json.Unmarshal(requestBytes, &amountField)
				Expect(err).ShouldNot(HaveOccurred())
				amount, err := strconv.ParseFloat(amountField.CreditTransfers[0].Amount, 64)
				Expect(err).ShouldNot(HaveOccurred())

				updatedBalanceCents := getAccountBalanceByCompanyName("COMPANY 1")
				Expect(updatedBalanceCents).ShouldNot(BeNil())

				Expect(res.StatusCode).Should(BeEquivalentTo(http.StatusCreated))
				Expect(*originalBalanceCents - int64(amount*100)).Should(BeEquivalentTo(*updatedBalanceCents))
			})
		})
	})
})
