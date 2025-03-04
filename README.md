This project is a backend implementation for a Music Streaming App. The backend exposes REST endpoints to manage subscriptions, which are consumed by a separate frontend system. All subscription data is stored in an SQL database, and the solution adheres to the specified business rules and requirements.

The application includes functionality for adding and deleting subscriptions, with validations and constraints as described below.

#Features
1. Add a Subscription
  Allows users to add new subscriptions to the platform. The following business rules apply:
  Validation Rules:
  If there is already a subscription with the same userId, plan, and startDate, the request will be rejected.
  The user will receive the message: "There is already a subscription with the same user, plan, and start date."

Request Structure:
  # Music Streaming App - Backend Implementation

## Add a Subscription - Request Structure

| Field Name   | Present on Request | Mandatory on Request | Present on Response | Other Validations                                                                 |
|--------------|--------------------|-----------------------|---------------------|----------------------------------------------------------------------------------|
| `id`         | No                 | No                    | Yes                 | PK, auto-generated at the database level                                         |
| `userId`     | Yes                | Yes                   | Yes                 | Long positive value                                                              |
| `plan`       | Yes                | Yes                   | Yes                 | Must be one of the following values: `FREE`, `PREMIUM`, `FAMILY`                |
| `billingCycle` | Yes              | Yes                   | Yes                 | Must be one of the following values: `MONTHLY`, `YEARLY`                        |
| `price`      | Yes                | Yes                   | Yes                 | Positive double value                                                            |
| `startDate`  | Yes                | Yes                   | Yes                 | LocalDate value                                                                  |
| `endDate`    | Yes                | No                    | Yes                 | LocalDate value                                                                  |
| `status`     | No                 | No                    | Yes                 | Must be one of the following values: `PENDING`, `ACTIVE`, `EXPIRED`, `CANCELLED`. If missing, defaults to `PENDING`. |

---

## Delete a Subscription - Conditions for Deletion

- The deletion is allowed only if the subscription's status is either:
  - **EXPIRED**
  - **CANCELLED**

- **Response Behavior:**
  - If deletion is successful: Returns a successful HTTP code (e.g., 200 OK) with no body.
  - If deletion is not allowed: Returns the message:  
    *"The subscription is used; it cannot be deleted."*


2. Delete a Subscription
  Allows users to delete subscriptions stored in the database. The following rules apply:
  Conditions for Deletion:
  The deletion is allowed only if the subscription's status is either: EXPIRED / CANCELLED
  If deletion is successful: The response contains only a successful HTTP code (e.g., 200 OK) with no body.
  If deletion is not allowed:  The response contains the message: "The subscription is used; it cannot be deleted."

Unit Tests
Unit tests have been implemented for service methods, ensuring coverage of at least 70% per branch.
