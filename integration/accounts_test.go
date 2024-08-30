//go:build integration

package integration

import (
	"context"
	"database/sql"
	"testing"

	_ "github.com/onsi/ginkgo/v2"
	_ "github.com/onsi/gomega"
	"github.com/stretchr/testify/require"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	_ "gorm.io/driver/mysql"
)

func TestSearchAccount(t *testing.T) {
	compose, err := tc.NewDockerComposeWith(tc.WithStackFiles("../docker-compose.yml"))
	if err != nil {
		require.NoError(t, err, "NewDockerComposeAPI()")
	}
	t.Cleanup(func() {
		require.NoError(t, compose.Down(context.Background(), tc.RemoveOrphans(true), tc.RemoveImagesLocal), "compose.Down()")
	})
	ctx, cancel := context.WithCancel(context.Background())
	t.Cleanup(cancel)
	require.NoError(t, compose.Up(ctx, tc.Wait(true)), "compose.Up()")
	sqlClient, err := sql.Open("mysql", "root:root@tcp(127.0.0.1:3307)/transfers_db")
	require.NoError(t, err)
	defer func() {
		require.NoError(t, sqlClient.Close())
	}()
	require.NoError(t, sqlClient.Ping())
}
