Feature: Validate XTV (Xfinity TV) PIL (https://www.teamccp.com/confluence/pages/viewpage.action?spaceKey=XBO&title=WebView+authentication+for+XfinityTV+mobile+app)

@WIP
@Integration
Scenario: Validate XTV (Xfinity TV) PIL (Comcast User)
Given an active customer
  And a web browser
  #1) Login
When user attempts to go to the XTV PIL page as partner 'comcast' with redirect url 'https://www.google.com'
  And user signs in at Login Page
  #2) Redirect to Google
  And confirm that 'Google' is the title of the web page
  And confirm that parameter 'responseCode' with value '200' appears in the browser's URL
  And confirm that parameter 'activationCode' appears in the browser's URL
  #3) Go to Login Page But Auto-Redirect to Google
Then user attempts to go to the XTV PIL page as partner 'comcast' with redirect url 'https://www.google.com'
  And confirm that 'Google' is the title of the web page
  And confirm that parameter 'responseCode' with value '200' appears in the browser's URL
  And confirm that parameter 'activationCode' appears in the browser's URL
  #4) Logout
  And attempt to go to the XfinityTV Partner Integration Layer logout page with post-login redirect to 'https://www.google.com'
  #5) Redirect to Google
  And confirm that 'Google' is the title of the web page
  #6) Go to Login Page Then Confirm No Auto-Redirect to Google
  And user attempts to go to the XTV PIL page as partner 'comcast' with redirect url 'https://www.google.com'
  And confirm that 'Sign in to Comcast' is the title of the web page
  And confirm user has signed out

@WIP
@Integration
Scenario: Validate XTV (Xfinity TV) PIL (Comcast User, Forced Authentication)
Given an active customer
  And a web browser
  #1) Login
When user attempts to go to the XTV PIL page as partner 'comcast' with redirect url 'https://www.google.com'
  And user signs in at Login Page
  #2) Redirect to Google
  And confirm that 'Google' is the title of the web page
  And confirm that parameter 'responseCode' with value '200' appears in the browser's URL
  And confirm that parameter 'activationCode' appears in the browser's URL
  #3) Go to Login Page Using Forced Authentication, But Even Though User Is Already Logged In, User Will Be Forced To Login Before Forwarding To Google
Then user attempts to go to the XTV PIL page as partner 'comcast' using forced authentication with redirect url 'https://www.google.com'
  And user signs in at Login Page with just a password
  And confirm that 'Google' is the title of the web page
  And confirm that parameter 'responseCode' with value '200' appears in the browser's URL
  And confirm that parameter 'activationCode' appears in the browser's URL
  #4) Logout
  And attempt to go to the XfinityTV Partner Integration Layer logout page with post-login redirect to 'https://www.google.com'
  #5) Redirect to Google
  And confirm that 'Google' is the title of the web page
  And confirm user has signed out

@WIP
@Integration
Scenario: Validate XTV (Xfinity TV) PIL (University Student - Emerson College)
Given an active customer
  And a web browser
  #1) Attempt to go to the University Login page which forwards browser to University selection page
When user attempts to go to the XTV PIL page as partner 'university' with redirect url 'https://www.google.com'
  #2) On the university selection page, select 'Emerson College'
  And on the 'XFINITY On Campus' page, select 'Emerson College'
  #3) Confirm that the browser has been redirected to the 'Emerson College Login' page, then try to login as a student
  And on the 'Emerson College Login' page, login using a student account
  #4) Confirm that the browser has forwarded to Google
Then confirm that 'Google' is the title of the web page
  And confirm that parameter 'responseCode' with value '200' appears in the browser's URL
  And confirm that parameter 'activationCode' appears in the browser's URL
