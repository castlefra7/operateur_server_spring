# Base de données
- Customers
    - id
    - name
    - email
    - phone_number

- Admin
    - id
    - name
    - level

- offer_types:
    - id
    - name
    - unit_name enum (mega, seconde, nombre)

- offers:
    - id 
    - name
    - created_at
    - offer_type_ids
    - duration   (day)
    - parent_offer_ids
    - amounts
    - cons_interior
    - cons_exterior
    - price
    - limit_per_period
    - limit_period (in days)

- subscriptions:
    - id 
    - name
    - created_at
    - offer_type_ids
    - duration   (days)
    - amounts
    - price
    - prior_notice_duration

- normal_offer:
    - id
    - offer_type_id
    - cons_interior
    - cons_exterior

- customer_consumptions:
    - id
    - id_customer
    - created_at
    - offer_type_id 
    - consumption_amount

- phone_calls:
    - id 
    - id_sender
    - phone_number_receiver
    - duration
    - created_at

- messages:
    - id 
    - id_sender
    - phone_number_receiver
    - text
    - created_at

- customer_offers: 
    - id
    - id_sender
    - id_receiver
    - created_at
    - id_offer

- deposits
    - id
    - created_at
    - id_customer
    - amount

- withdrawals: 
    - id
    - created_at
    - id_customer
    - amount
    - fees

- customer_transactions: (mobile money, crédit)
    - id 
    - created_at
    - id_sender
    - id_receiver
    - type (money, credit)
    - amount
    - fees

- sos_credit:
    - id
    - created_at
    - id_customer
    - borrowed_amount
    - remaining_amount

- unit_types:
    - id
    - nom
    - amount

- default_config:
    - id
    - type
    - unit

    - amount_per_unit_intra
    - amount_per_unit_extra

# Data
- Offre 500 Ar: 10 Messages et/ou 10 Mega pour 7 jours  
- Offre 1000 Ar: 10 mn Appel et/ou 10 Mega pour 1 jour
- Offre 2000 Ar: 2mn Appel et 10 Mega et 2 Messages pour 2 jours
- Offre 1000 Ar: 10 Go Facebook pour 7 jours
- Offre 2000 Ar: 5000 Ar d'appel pour 1 jour, 1Ar/s si interne, 5Ar/s si externe

- Achat offre, crédit pour mon numéro ou autre numéro 

- 3Ar/s, 1,5Ar/ko, 100Ar/140c