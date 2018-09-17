# Test Mock Service

## Get Started (incomplete)

- Create a project in GCP.
- Create a service account key.
- Save the key (json file) somewhere useful.
- export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/key.json
- export GCP_PROJECT_ID=your-project-name
- `mvn install`
- `mvn spring-boot:run`

## What Does It Do?
This application consumes mock API data from Mockaroo and outputs the data to /us-countries endpoint.
This application also now outputs/publishes the data to Google Cloud Platform (GCP) Event Streaming service Pub/Sub.

