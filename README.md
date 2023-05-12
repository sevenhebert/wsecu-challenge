# Vending Machine App #

## Running the application

The easiest way to run the service locally is to use docker-compose: `docker-compose up --build`.
For alternative build options see the README's in each of the project subdirectories.
Once everything has built, the migration has run, and the services are up, you can access the frontend
at [http://localhost:3000](http://localhost:3000).

## DESIGN ##

- UI/SPA displays product inventory information and presents available purchase actions to user.
  Uses vending machine context to store and update the state. Context/state is injected into the main "VendingMachine"
  page which passes state and handlers as props to function components that need them. Context instantiates an API class
  which it uses to make requests to the backend service.
  
- REST-ful backend service that processes orders and records transactions. Cake pattern used for dependency injection.
  Two controllers, one containing the APIs needed to by the vending machine client, and another serving admin/auditing 
  functionality (currently unauthenticated although JWT based auth middleware is in place if needed). The service layer
  implements the business logic, purchases are transactional so any failed subquery will rollback the entire transaction.
  Persistence layer is modeled after ERD and implemented with prepared statements to prevent SQL injection attacks. 

- Relational database to persist inventory and order history. See database/README.md for more details.

## IMPROVEMENTS ##

- Unit tests
- Explicit input validation
- More detailed error handling
- Backend service should validate payment amount
- Implement refund functionality, currently a stub


## ASSUMPTIONS ##

- The vending machine has a payment processor that handles the payment process
- This payment processor calculates and verifies the payment amount
