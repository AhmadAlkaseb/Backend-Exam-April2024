# Opgave 1
**Svar:** Færdig.
# Opgave 2
**Svar:**
### 2.1
    HTTP Method         REST Resource             Exception and status code

    GET                 /api/cars                 APIException & 404
    GET                 /api/cars/{id}            APIException & 404
    POST                /api/cars/                APIException & 500
    PUT                 /api/cars/{id}            APIException & 404
    DELETE              /api/cars/{id}            APIException & 404

# Opgave 3
**Svar:** Færdig.
# Opgave 4
**Svar:**
### Get all the cars
GET http://localhost:7007/api/cars svar: [
{
"id": 7,
"brand": "Brand 3",
"model": "Model 3",
"make": "Make 3",
"year": 2028,
"firstRegistrationYear": [
2028,
3,
3
],
"price": 503.0,
"sellerEmail": null
},
{
"id": 9,
"brand": "Brand 5",
"model": "Model 5",
"make": "Make 5",
"year": 2032,
"firstRegistrationYear": [
2032,
5,
5
],
"price": 505.0,
"sellerEmail": null
},
{
"id": 5,
"brand": "Brand 1",
"model": "Model 1",
"make": "Make 1",
"year": 2024,
"firstRegistrationYear": [
2024,
1,
1
],
"price": 501.0,
"sellerEmail": null
},
{
"id": 3,
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 120000.0,
"sellerEmail": null
},
{
"id": 6,
"brand": "Brand 2",
"model": "Model 2",
"make": "Make 2",
"year": 2026,
"firstRegistrationYear": [
2026,
2,
2
],
"price": 502.0,
"sellerEmail": null
},
{
"id": 8,
"brand": "Brand 4",
"model": "Model 4",
"make": "Make 4",
"year": 2030,
"firstRegistrationYear": [
2030,
4,
4
],
"price": 504.0,
"sellerEmail": null
}
]
### Get car by id
GET http://localhost:7007/api/cars/3 svar:{
"id": 3,
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 120000.0,
"sellerEmail": null
}
### Create car
POST http://localhost:7007/api/cars

{
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"firstRegistrationYear": "2024-03-04",
"price": 120000
} svar: {
"id": 10,
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 120000.0,
"sellerEmail": null
}
### Update existing car by id
PUT http://localhost:7007/api/cars/8

{
"brand": "TEST",
"model": "TEST",
"make": "TEST",
"firstRegistrationYear": "2024-03-04",
"price": 5000
} svar: {
"id": 8,
"brand": "TEST",
"model": "TEST",
"make": "TEST",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 5000.0,
"sellerEmail": null
}
### Delete existing car by id
DELETE http://localhost:7007/api/cars/7
svar: {
"id": 7,
"brand": "Brand 3",
"model": "Model 3",
"make": "Make 3",
"year": 2028,
"firstRegistrationYear": [
2028,
3,
3
],
"price": 503.0,
"sellerEmail": null
}
### Group by year
GET http://localhost:7007/api/cars/grouping/2024 svar: [
{
"id": 3,
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 120000.0,
"sellerEmail": null
},
{
"id": 8,
"brand": "TEST",
"model": "TEST",
"make": "TEST",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 5000.0,
"sellerEmail": null
},
{
"id": 5,
"brand": "Brand 1",
"model": "Model 1",
"make": "Make 1",
"year": 2024,
"firstRegistrationYear": [
2024,
1,
1
],
"price": 501.0,
"sellerEmail": null
},
{
"id": 10,
"brand": "Merrrcedes",
"model": "A-Class",
"make": "Mercedes-Benz",
"year": 2024,
"firstRegistrationYear": [
2024,
3,
4
],
"price": 120000.0,
"sellerEmail": null
}
]
# Opgave 5
**Svar:**
Jeg går ud fra at der menes forskellen på test af endpoints og dao. Man tester dao'en for at sikre at den virker i forhold til databasen.
Når man tester endpoints tjekker man for følgende:
1) Data validering.
2) Response output
3) Fejlbeskeder
# Opgave 6
**Svar:** Færdig.

### Udarbejdet af

_Ahmad Alkaseb_