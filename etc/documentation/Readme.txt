
For some reason, even though swagger has picked up by spring, its not reading the controllers to provide the documentation.

1. Scripts in scripts folder will set up mySQL database with users and products
2. User needs to be logged in to post review
3. User need not be logged in for retrieving product reviews. But if logged in, with session id cookie, reviews can be edit/deleted.

Examples of rest url's
1. Login
Url :http://localhost:8080/review-services/v1/login
Http method : POST
Http Headers : Content-Type: application/json
Http Content : {"username":"karthik","password":"password"}


2. Get product
Url :http://localhost:8080/review-services/v1/products/{productId}
Http method : GET
Http Headers : Content-Type: application/json

3. Get all products

Url :http://localhost:8080/review-services/v1/products/
Http method : GET
Http Headers : Content-Type: application/json


4. Get review
Url :http://localhost:8080/review-services/v1/reviews/{reviewId}
Http method : GET
Http Headers : Content-Type: application/json


5. Post a review
Url :http://localhost:8080/review-services/v1/reviews/
Http method : POST
Http Headers : Content-Type: application/json
			   service-session-id=abc-def-ghi	
Http content:
{
	"comment": "Review 2",
	"rating": "3",
	"profileId": "10000",
	"productId": "100"
}

6. Edit a review
Url :http://localhost:8080/review-services/v1/reviews/
Http method : PUT
Http Headers : Content-Type: application/json
				service-session-id=abc-def-ghi
Http Content:
{
	"comment": "Review 18",
	"rating": "4",
	"productId": "200" 
}



7. Delete a review
Url :http://localhost:8080/review-services/v1/reviews/{reviewId}
Http method : DELETE
Http Headers : Content-Type: application/json
				service-session-id=abc-def-ghi


