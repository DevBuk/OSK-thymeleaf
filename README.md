# OSK-thymeleaf
Web application for servicing students in the driver training center. The purpose of the application is to solve the problem related to the manual enrollment by students in a driver training center for given driving hours.
The application allows the user-customers to:
- logging in and logging out of the application
- selecting the "Remember me" option when logging in
- signing up for lessons with instructors of user choice on days and hours that are convenient for user.
- displaying a summary of user lessons
- editing selected lessons (e.g. canceling lessons, changing lesson hours)
The administrator of the application is possible:
- logging in and logging out of the application
- selecting the "Remember me" option when logging in
- displaying a summary of the lessons for the whole driving school
- editing selected records ()
- adding new lessons for selected users and instructors
- performing CRUD operations on users
- performing CRUD operations on instructors

The application uses the MVC (Model-View-Controller) architectural pattern. The "Thymeleaf" technology was used to display the views, thanks to that it was possible to implement, among others, the following features:
- users are displayed only available driving hours for the selected instructor and the day (the hours booked by other users for the user are not visible)
- for the administrator, for the selected user, instructor and day, only available driving hours are displayed (hours booked by other users are invisible - from the level of selected user by administrator)
