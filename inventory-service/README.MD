# Design

We'd like to go with model-driven development for the inventory service. So we start with a model. The inventory should be the model. What we want to support for inventory is the ability to change the quantity of the inventory.

## Domain Model
The domain will have the attributes as below:
- sku: String
- location: Integer
- quantity: Integer
- type: enum of store, DC or EFC

I'd like to have a composite key of sku/location pair to identify the model, essentially we want to have the inventory of the sku **a** at the location **x**. 

## Model Mapper
The database I choose is postgres, will use a UUID for the database identifier.

## Service Layer
- Retrieve an inventory for sku **a** at location **x**
- Retrieve all inventory for a location or sku with pagenation
- Add an inventory to the specific location (DC, EFC or Store)
- Out stock an inventory 
- Delete an inventory
- Test if the inventory is available
- Test if there is enough inventory

## DTO and Controller

# Entity versioning
