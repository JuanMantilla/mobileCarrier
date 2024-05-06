# Mobile Carieer app (backend)

This backend app allows you to create users, daily usages with mb used and keep track of user's billing cycles based on the daily usages.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
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

Using docker:
    - Go to the docker directory and use "docker compose up". This will create the Mongo DB and Mobile carieer app containers, it will also seed the DB with initial data.
Locally:
    - mvn pacakge
    - mvn compile
    - mvn clean install
    - mvn spring-boot:run



## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
