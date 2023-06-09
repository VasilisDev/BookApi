# Book Application

This project is a book management application.

## Getting Started

To get started with the Book Application, follow the steps below:

1. **Clone the repository:**

   ```shell
   git clone https://github.com/VasilisDev/BookApi.git

2. **Navigate to the project directory:**

    ```shell
    cd [project directory]

3. **Build and run the application using Docker:**

    To build and run the application using Docker, please follow the instructions below:
    
    **Prerequisites:**
    
    - Docker must be installed on your system. If you haven't installed Docker yet, please refer to the official Docker documentation for installation instructions specific to your operating system.
  
    **Build and Run Steps:**
   
    - Open a terminal or command prompt.
    
    - Navigate to the project directory where the start.sh file located.
    
    - Depending on your operating system, execute one of the following commands:
    
        - For Linux: ./start.sh
        - For Windows: start.sh
    
    - This will execute the start.sh script and initiate the build and run process.
    
    - Wait for the Docker build process to complete. This may take a few moments depending on your system and internet connection.
    
    - Once the build process finished, the Docker containers will be started in the background.

4. **Access the application:**

    Once the containers are up and running, you can now access the Book search app by opening a web browser and entering the appropriate URL. The exact URL will depend on your Docker setup and configuration. Typically, you can access the application using http://localhost:8080.
    
5. **Book Management API usages**

    Endpoints related to book management.

    - Search Books
    
         Endpoint: GET /api/books/search
    
         Searches for books based on the provided title.
    
         Parameters:
    
        - title (required): The title of the book to search for.
        - page (optional): The page number for pagination. Should be a positive number.
        - pageSize (optional): The number of books per page.
        
        Example Request:    
        ```http 
        GET http://localhost:8080/api/books/search?title=Little%20Women;%20Or,%20Meg,%20Jo,%20Beth,%20and%20Amy&page=1&pageSize=2
       ```
       
        Example Response: 
        ```http
        [
            {
                "id": 37106,
                "title": "Little Women; Or, Meg, Jo, Beth, and Amy",
                "authors": [
                    {
                        "name": "Alcott, Louisa May"
                    }
                ]
            }
        ]
       ```
      
   - Get Book Details
   
       Endpoint: GET /api/books/{bookId}
       
       Retrieves the details of a specific book.
       
       Parameters:
       
       bookId (required): The ID of the book to retrieve.
       Example Request:
       ```http 
       GET /api/books/4
       ``` 
     
       Example Response:   
       ```http 
       {
           "id": 4,
           "title": "Lincoln's Gettysburg Address: Given November 19, 1863 on the battlefield near Gettysburg, Pennsylvania, USA",
           "authors": [
               {
                   "name": "Lincoln, Abraham"
               }
           ],
           "rating": 2.5,
           "reviews": [
               "hello review",
               "hello review 4",
               "hello review 5",
               "hello review 1"
           ]
       }
       ```
   
   - Get Top Rated Books
   
       Endpoint: GET /api/books/top-rated
           
       Retrieves a list of top-rated books.
          
       Parameters:
           
       limit (required): The maximum number of books to retrieve. Should be a positive number.
       
       Example Request:
       ```http
       GET /api/books/top-rated?limit=5
       ```
           
       Example Response:
       ```http    
       [
         {
           "id": 100,
           "title": "The Complete Works of William Shakespeare",
           "authors": [
             {
               "name": "Shakespeare, William"
             }
           ]
         },
         {
           "id": 101,
           "title": "Effective Java 3rd edition",
           "authors": [
             {
               "name": "Bloch, Joshua"
             }
           ]
         }
       ]
       ```
    
   - Get Monthly Average Rating
   
       Endpoint: GET /api/books/{bookId}/monthly-average-rating
        
       Retrieves the monthly average rating for a specific book.
            
       Parameters:
            
       bookId (required): The ID of the book.
           
       Example Request:
        ```http
        GET /api/books/1/monthly-average-rating
        ```
            
       Example Response:
       ```http        
       {
            "bookId": 4,
            "yearlyMonthlyAverageRatings": [
                {
                    "year": 2023,
                    "monthlyAverageRatings": [
                        {
                            "month": 5,
                            "averageRating": 2.5
                        }
                    ]
                }
            ]
       }
       ```
         
   - Review a Book
         
      Endpoint: POST /api/review
       
      Review a book.
         
      Example Request:
      ```http
      POST /api/review
      ```
    
      Request Body:
      ```http   
       {
           "bookId": 1,
           "rating": 5,
           "reviewText": "Great book!"
        }
        ```