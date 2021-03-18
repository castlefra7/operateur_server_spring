# add column isValidated in table Deposit
# Automatic phone number generation
# Column in customer table phoenumber should be only the number of the customer without +26133
# What about the usage of preparedstatement in FctGen
# Quand effectuer transaction MVOLA alors entrer mot de passe
# HASH PASSWORD

# SETUP JWT in  spring boot
# create a web service for withdraw: simulation of JWT
# make web services work in spring boot (deposit, credit)
# appel, message, internet
# setup notification with firebase
# notification when achat de credit
# create message navigation in ionic
# notification when deposit, withdraw
# change prefix to requestmapping in SPRING



# FOR TOMORROW
    - isvalidated in table deposit => change all balance request of user
    - Base de données:
        - firebase_cloud_messaging: customer_id, app_id
        - JWT table:
    - IONIC:
        - Interface simulation: appel et internet
        - Interface pour se connecter
        - Notification firebase:
            - Interface espace client pour connaître solde crédit et solde mobile money (which should be firebase notification)
            - Interface liste des messages

    - JWT with spring boot

    - Working SPRING BOOT web services

# AFTER TOMORROW
- deploying to heroku
- notifications message with firebase when dépôt et retrait mobile money

# Friday:
- insert many data and test

# TODO SPRING BOOT:
- ENDPOINTS:
    - POST mobilemoney/deposit
        - date
        - phone_number
        - amount
    - POST mobilemoney/withdraw
        - date
        - phone_number
        - password
        - amount
    - POST mobilemoney/transfer
        - date
        - password
        - phone_number_source
        - phone_number_destination
        - amount
    - POST credit/buy
        - phone_number
        - amount
        - date
    - POST credit/transfer
        - phone_number
        - amount
        - password
        - date
        - phone_number_destination
    - GET customers

# response example:
status: {
    code:,
    message:
}, data: []


# ENDPOINTS
- GET /offers

- POST /offers
    - offer

- POST /offers/:offer_id/buy
    - phone_number

# ConsumptionController
- POST consumptions/calls
    - phone_number
    - phone_number_dest
    - duration
    - date

- POST consumptions/messages
    - phone_number
    - phone_number_dest
    - text
    - date

- POST consumptions/internet
    - phone
    - application_id
    - consumed_data

# OfferLogic
- when making calls, when sending messages, when browsing


# StatisticsController
- GET stats/offers

- GET stats/consumptions


# TarfisController
- POST tarifs/messages

- POST tarifs/internet

- POST tarifs/calls