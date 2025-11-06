# SpringBoot Note Manager

This program is a web Note Manager that uses Java with SpringBoot and Thymeleaf

You can do the following actions:
- Add a note
- Remove a note
- Edit a note (implemented with JS)
- View all the notes saves on your database

# How to run this project?

This project has a PS script that loads all environment variables in a .env file, if no .env file is found, it will load your system environment variables.

The .env file in your project should look like this:
```dotenv
DB_URL=jdbc:mysql://localhost:3306/your_database
DB_USER=your_db_user
DB_PASS=your_db_password
```

These variables are used by the application.properties file to access your database with your credentials

>[!NOTE]
> You can also just run the application class and it will run with default credentials