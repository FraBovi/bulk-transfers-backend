package integration_test

import (
	"testing"

	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
)

type account struct {
	Id               int    `json:"id"`
	OrganizationName string `json:"organizationName"`
	BalanceCents     string `json:"balanceCents"`
	Iban             string `json:"iban"`
	Bic              string `json:"bic"`
}

func TestIntegration(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Integration Suite")
}
