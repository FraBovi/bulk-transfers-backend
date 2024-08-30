//go:build integration

package integration

import (
	"context"
	"testing"

	"github.com/stretchr/testify/require"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
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
}
