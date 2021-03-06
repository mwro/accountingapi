# accountingapi

Simple accounting API for money transfers. Based on Spark web framework.

Allows adding new users, viewing them and transfering money between them.
In-memory data storage. 


To add a user, use HTTP POST method for URL /accounts with body specifying a user's name in JSON, e.g:

{
	"name": "Marcin"
}

To get all accounts, use HTTP GET method with URL /accounts or /accounts/id to get user with specified id

To transfer money, use HTTP POST method for URL /moneytransfers with body specifying transfer details in JSON, e.g:

deposit:
{
	"accountToID": 0,
	"transferValue": 1000
}

withdrawal:
{
	"accountFromID": 0,
	"transferValue": 1000
}

transfer between accounts:
{
	"accountFromID": 0,
	"accountToID": 2,
	"transferValue": 500
}

To get all money transfers, use HTTP GET method with URL /moneytransfers or /moneytransfers/id to get moneytransfer with specified id

Port: 4567
Status codes:
200 - Success
400 - Bad request
404 - Not found

You may want to check test classes to see possible use cases.
