# Guwon_App

## Idea

A trainer of a martial arts club in my hometown talked to me one day about the organisation in this club.
He told me about aspects where he could imagine a technological tool potentially being very handy. We started
brainstorming about features, about the look and feel of the tool and what problems may arise. We talked about
basic features like the most recent updates concerning the club, possibilities to rate the students in different
aspects, communication tools and gamification ideas to motivate the students to put more effort into the 
theoretical part of the martial art.
Then I started to develop this application on my own.

## Technology

### User interface:
I used Kotlin to create the main application. Kotlin is the programming language google launched to develop smartphone
applications specifically designed for android. It works smoother with the android iOS than Java, which has been the 
state-of-the-art programming language for this purpose before. 

### Backend:
I used the MySQL-database of a webhosting server which i rented for testing server communication and for practicing web
development. However, because of the very nature of webhosting, I was not able to utilize a framework like laravel, how
I originally planned to do. So the PHP-code has been done fairly simple. Because of the lack of security configurations
it is not advisable to use this approach in a released version. For demonstating the own experience with PHP and different
algorithmic challenges it is acceptable, though. 

## Content
This version contains several different pages:
The superficial part, that is available to anyone using the app, contains pages like:
Markup : * General
             * News, Team, Schedules of the different arts, important dates, contact form
             * General information about every martial arts offered
             * A messaging functionality
                 
For people who are members of the club there is a profile part, where they find more pages:
Markup : * Profile
             * Overview of their rang and their gamification status
             * Search function, attendence, events, calendar
             * Quizes, to test the theoretical parts regularly
             * high score for the quizes 
                 * This was supposed to be the base for a real life price at the end of every season

## Annotations
While I put a lot of time and work into this project, it still does not reflect my current state of skill
in creating applications. I started this project with just an idea and no experience of creating fairly
complex projects on my own. So I had to read a lot, revise/redo a lot and experience a ton of different and
difficult bugs. This did help me getting better in coding and the functionalities do more or less work. 
However, the structure of the project is not optimal, the user experience is somehow childish and some bugs
are still present in this version. 
At some point we stopped working on this project because we realized that the demand is not as strong as we 
thought and turned to other projects. 

Hence, the bottom line is: This has been my very first own project of a larger scale and it is a very vivid example
how I started coding on my own, organising myself and getting to know user experience while struggling with
front- and backend at the same time. It is advisable to compare it to my latest project as it is apparent, that 
I learned a lot and started structuring projects far better from the very start afterwards. 

## Launching the Project
This application works best with the Android Studio IDE. The newest version should enable launching this app on a
smartphone emulator, at least with the version of the 20. August 2021, however it is very likely, that upcoming
released do not support deprecated functions anymore. 

## Screenshot

The Profile of the member            |  The upper part of the team page
:-------------------------:|:-------------------------:
<img src="https://github.com/Gauerdia/Guwon_App/blob/master/Readme_content/screennshot_profile.png?raw=true" width="300" height="580">  |  <img src="https://github.com/Gauerdia/Guwon_App/blob/master/Readme_content/screennshot_team.png?raw=true" width="300" height="580">
The contact functionality         
<img src="https://github.com/Gauerdia/Guwon_App/blob/master/Readme_content/screennshot_kontakt.png?raw=true" width="300" height="580">  
