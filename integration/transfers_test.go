//go:build integration

package integration_test

import (
	"context"
	"database/sql"
	"os"

	"github.com/docker/docker/errdefs"
	. "github.com/onsi/ginkgo/v2"
	. "github.com/onsi/gomega"
	"github.com/ossan-dev/bulktransfertests/helpers"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
)

// NICE2HAVE: start compose stack only once
var _ = Describe("transfers", Ordered, func() {
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
		sqlClient, err := sql.Open("mysql", "root:root@tcp(127.0.0.1:3307)/transfers_db")
		Expect(err).Should(BeNil())
		DeferCleanup(func() {
			Expect(sqlClient.Close()).Should(BeNil())
		})
		Expect(sqlClient.Ping()).Should(BeNil())
	})

	Describe("test", func() {
		It("test", func() {})
	})
})
