# Integration tests

The idea behind these integration tests is to provide a way to test the HTTP endpoints out. Everything needed to run the tests live within the `integration` folder.

## How to run them

Let me walk you through the setup needed to run the integration tests. The steps are:

1. Install Go on your machine. Check this [link](https://go.dev/doc/install) out
2. Install the Ginkgo Testing Framework. More on that [here](https://onsi.github.io/ginkgo/#why-ginkgo)
3. To confirm your installation, make sure to run in your terminal the command `ginkgo version`
4. Navigate the `integration` folder
5. From your terminal run the command `go mod tidy` to make sure to download the needed dependencies
6. Start your test suite by running `ginkgo --tags=integration`
7. Eventually, you can also start the test from VSCode by selecting in the Debug View menu the `integration-test` profile

## Technical Specs

I used `ginkgo` and `gomega` to give them a try. This integration test project is a POC. Furthermore, the tests have been written against a `docker-compose.yml` file since I don't want to dig into the code. I took it for granted. What matters to me is the chance of building tests against an already "live" project with minimal setup. `testcontainers` ease a lot this process of setting containers up.
