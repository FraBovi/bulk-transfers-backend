//go:build integration

package integration_test

import (
	"context"
	"database/sql"
	"net/http"
	"os"
	"testing"

	"github.com/docker/docker/errdefs"
	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
	"github.com/ossan-dev/bulktransfertests/helpers"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
)

var (
	sqlClient  *sql.DB
	ctx        context.Context
	cancel     context.CancelFunc
	httpClient http.Client
)

func TestIntegration(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Integration Suite")
}

var _ = BeforeSuite(func() {
	os.Setenv("TESTCONTAINERS_RYUK_DISABLED", "true")
	composeReq, err := tc.NewDockerComposeWith(tc.WithStackFiles("../docker-compose.yml"))
	Expect(err).ShouldNot(HaveOccurred())
	DeferCleanup(func() {
		Expect(composeReq.Down(context.Background(), tc.RemoveOrphans(true), tc.RemoveImagesLocal)).To(Succeed())
	})

	ctx, cancel = context.WithCancel(context.Background())
	DeferCleanup(cancel)

	httpClient = http.Client{}

	composeErr := composeReq.
		WaitForService("api_service", wait.ForListeningPort("8080/tcp")).
		Up(ctx, tc.Wait(true))
	if composeErr != nil && errdefs.IsConflict(composeErr) {
		Expect(helpers.ReRunContainersAfterConflict(ctx, composeReq)).To(Succeed())
	}

	sqlClient, err = sql.Open("mysql", "root:root@tcp(127.0.0.1:3307)/transfers_db")
	Expect(err).ShouldNot(HaveOccurred())
	Expect(sqlClient.Ping()).To(Succeed())
})

var _ = AfterSuite(func() {
	Expect(sqlClient.Close()).To(Succeed())
})
