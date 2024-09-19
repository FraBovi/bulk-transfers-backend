package helpers

import (
	"context"

	"github.com/docker/docker/api/types/container"
	"github.com/docker/docker/client"
	tc "github.com/testcontainers/testcontainers-go/modules/compose"
	"github.com/testcontainers/testcontainers-go/wait"
)

type Account struct {
	Id               int    `json:"id"`
	OrganizationName string `json:"organizationName"`
	BalanceCents     string `json:"balanceCents"`
	Iban             string `json:"iban"`
	Bic              string `json:"bic"`
}

func ReRunContainersAfterConflict(ctx context.Context, composeReq tc.ComposeStack) error {
	dockerClient, err := client.NewClientWithOpts()
	if err != nil {
		return err
	}
	containers := composeReq.Services()
	for _, c := range containers {
		err = dockerClient.ContainerStop(context.Background(), c, container.StopOptions{})
		if err != nil {
			return err
		}
		err = dockerClient.ContainerRemove(context.Background(), c, container.RemoveOptions{})
		if err != nil {
			return err
		}
	}
	err = composeReq.
		WaitForService("api_service", wait.ForListeningPort("8080/tcp")).
		Up(ctx, tc.Wait(true))
	if err != nil {
		return err
	}
	return nil
}
