# Cognito POC

## Introduction
Welcome to Cognito POC (Proof of Concept)! This project demonstrates how to integrate AWS Cognito for authentication and user management in a Spring Boot application.

## Local Development
To run this project locally, follow these steps:

1. Set the active Spring profile to `local`:
   ```bash
   ./gradlew bootRun --args='--spring.profiles.active=local'

If you are not using the local profile, provide the necessary AWS Cognito environment variables:

    AWS_SECRET_KEY
    AWS_ACCESS_KEY
    AWS_COGNITO_POOL_ID
    AWS_CLIENT_ID

## AWS Cognito Configuration

For detailed instructions on how to obtain the required AWS Cognito environment variables (`AWS_SECRET_KEY`, `AWS_ACCESS_KEY`, `AWS_COGNITO_POOL_ID`, `AWS_CLIENT_ID`), please refer to our Confluence page:

[Aws Cognito Documentation](https://healthbookplus.atlassian.net/wiki/spaces/HEALTHBOOK/pages/419528708/Cognito+BE+application)
