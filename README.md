# Mobile Carieer app (backend)

This backend app allows you to create users, daily usages with mb used and keep track of user's billing cycles based on the daily usages.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Documentation](#documentation)
- [Thought process](#thought_process)
- [Improvements](#improvements)
- [License](#license)

## Introduction

This Mobile Carrier app (backend) is a backend application that allows you to create users, track their daily usages in megabytes (MB), and manage their billing cycles based on the daily usages. It provides a convenient way to keep track of user data usage and automate the billing process. This app is useful for mobile carriers or service providers who need to manage user accounts and billing efficiently.

## Features
- User creation: Create new users for the mobile carrier app.
- Daily usage tracking: Track the daily data usage of users in megabytes (MB).
- Billing cycle management: Manage the billing cycles of users based on their daily data usage.
- Automated billing process: Automate the billing process for users based on their data usage.



## Getting Started

### Prerequisites

- Java JDK 21
- Maven 3.2.5
- Docker (optional)
- Mongo DB

### Installation

#### Using docker
- Go to the docker directory and use "docker compose up". This will create the Mongo DB and Mobile carieer app containers, it will also seed the DB with initial data.

#### Locally
- mvn pacakge
- mvn compile
- mvn clean install
- mvn spring-boot:run (This will seed the DB with initial data)

## Documentation
### Documentation diagrams
 Can be found in the Documentation folder in the root of the app
### API documentation
 I used swagger to generate API documentation of the application. To view it, after the application is running, you can go to host:port/swagger-ui/index.html to view it.
## Thought process

### Requirements

1. **User Management:**
   - Create and update users with attributes: first name, last name, email, and password.

2. **Daily Usage Collection:**
   - Store daily usage data.
   - Record usage date, MB used, MDN (Mobile Directory Number), and user id.
   - This data is updated every 15 minutes

3. **Billing Cycle:**
   - Implement a billing cycle for each user and MDN.
   - Track user's current cycle's daily usage.
   - Retrieve user's cycle history.

### Technology Stack

- **Spring Boot:**
  - Chose Spring Boot for its simplicity and convention-over-configuration approach.
- **MongoDB:**
  - Utilized MongoDB as a NoSQL database for flexible schema design and scalability.
- **Maven:**
  - Used Maven as a build automation tool for managing dependencies.
- **Spring Boot Starter Logging:**
  - Configured logging for debugging and auditing purposes.
- **JUnit:**
  - Wrote unit tests to ensure correctness.
- **Mongock:**
  - Seeded the database with initial data for testing.
- **Swagger:**
  - Automatically generated API documentation.

### Data Modeling

- **User Model:**
  - Defined attributes: first name, last name, email, and password (hashed).
- **Daily Usage Model:**
  - Attributes included date, MB used, MDN, consumption time, and nextCycleId (for linking to the next billing cycle).
- **Cycle Model:**
  - Represented billing cycles.
  - Linked to daily usage records.

#### Data Modeling Considerations

1. **Schema Flexibility with MongoDB:**
   - MongoDB's schema flexibility allows designing data models without rigid table structures.
   - Unlike traditional relational databases, MongoDB accommodates evolving requirements and dynamic data.

2. **Document-Oriented Approach:**
   - MongoDB stores data in documents (in a JSON-like format), where each document represents an entity.
   - In this case:
     - **User Document:** Contains user-related information (first name, last name, email, password.).
     - **Daily Usage Document:** Holds daily usage data (date, MB used, MDN, usageDate.).
     - **Cycle Document:** Represents billing cycles.

3. **Why Link Daily Usage with Cycles?**
   - **1-to-Many Relationship:**
     - I established a 1-to-many relationship between cycles and daily usage:
       - Each cycle can have multiple daily usage records.
       - Each daily usage record belongs to a specific cycle.
     - This relationship organizes data efficiently and maintains referential integrity.
   - **Benefits:**
     - **Historical Context:**
       - By linking daily usage records to cycles, we can easily track usage history over time.
       - For example, retrieving all daily usage records associated with a specific cycle (e.g., monthly billing cycle) becomes straightforward.
     - **Simplified Queries:**
       - When creating or updating a daily usage record, we only need to reference the next cycle (via `nextCycleId`).
       - No need to query the entire cycle collection to find the current cycle.
     - **Efficient Aggregation:**
       - Aggregating daily usage data (e.g., calculating total usage for a cycle) becomes efficient.
       - It's possible to perform aggregation operations directly on the linked data.

4. **Security and Performance:**
   - Storing hashed passwords for users enhances security.
   - Efficiently querying daily usage data for a specific cycle minimizes database load.

5. **Sharding**

   - **What is Sharding?**
     - Sharding involves horizontally partitioning data across multiple servers (shards) in a distributed database system.
     - Each shard holds a subset of the data, allowing for scalability and improved performance.
     - MongoDB supports sharding to handle large datasets and high workloads.

   - **Shard Keys:**
     - I've defined shard keys for two collections:
        - **Daily Usage Collection:**
          - Shard key: `userId` and `mdn`.
          - Data for a specific user and MDN resides together on the same shard.
        - **Cycle Collection:**
          - Also uses shard key: `userId` and `mdn`.
          - Consistent shard keys ensure related data stays on the same shard.

    - **Horizontal Scalability:**
      - Sharding based on less-repeating values (like `userId` and `mdn`) ensures better data distribution.
      - As the dataset grows, adding more shards handles increased load.
      - Sharding enables horizontal scaling, distributing workload across servers.

6. **Indexing**

   - **Why Use Indexes?**
      - Indexes enhance query performance by enabling efficient data retrieval.
      - Without indexes, MongoDB would need to scan the entire collection for matching documents.
      - Indexes speed up queries by quickly locating relevant data.

   - **Indexed IDs:**
      - MongoDB automatically creates an index on the `_id` field (corresponding to your `id` field).
      - Ensures fast lookups based on these IDs.

   - **Considerations:**
      - Balance read and write performance when creating indexes.
      - Indexes improve reads but slightly impact writes (due to index maintenance).


### Configuration

- Used a `.env` file for MongoDB configuration:
  - Set MongoDB port, username, password, database name, logging level, and app port.
  - Exported these variables locally for development.

### Layers

1. **Repository Layer:**
   - Defined repositories for each document (user, daily usage, cycle).
   - Extended `MongoRepository` for easy data access.

2. **Service Layer:**
   - Implemented business logic.
   - Created services for each controller.

3. **Controller Layer (REST APIs):**
   - Handled incoming HTTP requests.
   - Mapped endpoints to service methods.
   - Validated input data.

### Testing

- Wrote unit tests for service layer components.
- Ensured correctness and robustness.

### Logging and Monitoring

- Incorporated logging statements throughout the application.
- Saved logs to a rotating file (up to 10 MB) with compression.

### Deployment

- Used Docker Compose:
  - Set up MongoDB container with persistent storage.
  - Configured the app container to connect to MongoDB.
  - Started the application.

## Improvents
1. **Stand-Alone Cron Job for Daily Usages:**
   - If the process of creating or updating daily usages is external (e.g., data ingestion from another system), consider implementing it as a stand-alone cron job.
   - Benefits:
     - **Efficiency:** The cron job can process data in batches, reducing the overhead of real-time API requests.
     - **Good Practice:** Separating this process ensures cleaner code and adheres to best practices.
     - **Faster API Responses:** When API endpoints are used, they won't be slowed down by data processing.

2. **Linked List for Daily Usages:**
   - Consider using a linked list structure for daily usages, where each usage record points to the previous and next usage.
   - Advantages:
     - **Eliminate Querying Previous Day's Usage:** With linked references, querying the previous day's usage becomes unnecessary during creation or update.
     - **Efficient Navigation:** Easily traverse daily usages in chronological order.
     - **Simplified Logic:** No need to search for adjacent records; the linked structure provides direct access.

3. **Pagination (If Requirements Allow):**
   - If the project requirements permit, implement pagination for API responses.
   - Benefits:
     - **Reduced Data Transfer:** Paginated responses send only a subset of data, improving network efficiency.
     - **Better User Experience:** Users receive manageable chunks of data, especially for large collections.
     - **Scalability:** Pagination supports handling large datasets without overwhelming the client.
4. Security layer with API an API key.
5. Delete methods in the API.

## Additional Features (Time-Based)

1. **Cache System Using Redis:**
   - Integrate Redis as a caching layer.
   - Use Redis to store frequently accessed data (e.g., cycle history, users, frequently queried results).
   - Benefits:
     - **Faster Responses:** Cached data reduces database queries, improving response times.
     - **Offload Database:** Lightens the load on MongoDB.
     - **Expiration Policies:** Set cache expiration to keep data fresh based on how frequent the API is consumed.

2. **Data Tests:**
   - Add comprehensive data testing:
     - **Integration Tests:** Verify interactions between the services, repositories and DB

3. **Secret Credential Management Using Kubernetes:**
   - Implement secure credential management for sensitive information (e.g., database credentials, API keys) instead of using a .env file.
   - Utilize Kubernetes secrets to store and manage secrets securely.
   - Benefits:
     - **Security:** Protect sensitive data from exposure.
     - **Centralized Management:** Kubernetes secrets provide a centralized way to manage credentials.

4. **Auditing Database Changes:**
   - Implement an auditing mechanism to track changes in the database.
   - Capture details such as who made the change, when, and what was modified.
   - Useful for debugging, compliance, and security purposes.


## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
