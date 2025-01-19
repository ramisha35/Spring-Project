# Spring-Project

გასაშვებად შეგიძლიათ გამოიყენოთ კომანდ პრომპტში..

curl -X POST http://localhost:8081/api/transfers/optimize -H "Content-Type: application/json" -d "{\"maxWeight\": 15, \"availableTransfers\": [{\"weight\": 5, \"cost\": 10}, {\"weight\": 10, \"cost\": 20}, {\"weight\": 3, \"cost\": 5}, {\"weight\": 8, \"cost\": 15}]}"