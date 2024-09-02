//go:build integration

package integration

import (
	"context"
	"database/sql"
	"net/http"
	"testing"
	"time"

	"github.com/stretchr/testify/require"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
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
	err = compose.WaitForService("api_service", wait.NewHTTPStrategy("/").WithPort("8080/tcp").WithStartupTimeout(10*time.Second)).Up(ctx, tc.Wait(true))
	require.NoError(t, err)
	sqlClient, err := sql.Open("mysql", "root:root@tcp(127.0.0.1:3307)/transfers_db")
	require.NoError(t, err)
	defer func() {
		require.NoError(t, sqlClient.Close())
	}()
	require.NoError(t, sqlClient.Ping())
	client := http.Client{}
	r, err := http.NewRequest(http.MethodGet, "http://127.0.0.1:8080/api/accounts", nil)
	require.NoError(t, err)
	res, err := client.Do(r)
	require.NoError(t, err)
	require.Equal(t, http.StatusOK, res.StatusCode)
}
