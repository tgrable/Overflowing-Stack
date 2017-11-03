# Overflowing-Stack

Android app that displays a list of recent Stack Overflow (Android tagged) questions that have an accepted answer and
contain more than 1 answer. Visitors can view all the answers for a selected question and try to guess
which answer was the accepted answer. Any guessed answers will be saved to a sqlite database until the user refreshes the questions/answers.

## Features
1. Display a list of recent questions that have an accepted answer and more than 1 answer
2. Searching / filtering based on post title
3. Display list of recently guessed questions
4. Select a question from either of the lists above to view all the answers for that question
5. Visitors can guess which answer they think is the “accepted” answer
6. After guessing, the correct answer is highlighted in green
7. The users score is kept based on the up votes minus the down votes of the guessed answer 
