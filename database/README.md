In this ERD, there are four entities:

PRODUCT: Represents a product in the vending machine, with properties for the product name, price, and quantity.
TRANSACTION: Represents a transaction in the vending machine, with properties for the transaction date and the amount paid by the customer.
LEDGER: Represents a product purchased in a transaction, with properties for the quantity purchased and a foreign key to the transaction and product entities.
PAYMENT: Represents a payment made by the customer, with properties for the transaction ID and the payment amount.

+----------------+       +----------------+           +----------------+       +----------------+
|    PRODUCT     |       |  TRANSACTION   |           | LEDGER         |       | PAYMENT        |
+----------------+       +----------------+           +----------------+       +----------------+
| id: Int        | 1..*  | id: Int        |           | txn_id: Int    |       | id: Int        |
| name: String   |<>-----| total: Double  | 1..*      | product_id: Int|       | txn_id: Int    |
| price: Double  |       | date: DateTime |<>---------| quantity: Int  |       | amount: Double |
| quantity: Int  |       +----------------+           +----------------+       +----------------+
+----------------+

The relationships between the entities are as follows:

PRODUCT and LEDGER have a one-to-many relationship, as one product can be purchased in many transactions, but each transaction only contains one product.
TRANSACTION and LEDGER have a one-to-many relationship, as one transaction can contain many products, but each product is only purchased in one transaction.
TRANSACTION and PAYMENT have a one-to-one relationship, as each transaction corresponds to one payment.
