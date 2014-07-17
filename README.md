Dopamine API Android Client
======================

#Quick Start

You can get started with Dopamine in your Android app in just a few minutes. Here’s how:


1.	Download and install this API Client.
2.	Run the Live Example App to immediately get a feel for how to use the API.
3.	Use the Life Example App as a template for how your app can use the Dopamine API.


**Let’s get started!**

<hr>

###Add the Files

Our recommended way of installing the Dopamine API Client is to use the JAR file we've included in this repo. 

We have included two sets of files: a completed, *Live Example App* you can import into Eclipse and run immediately, and the core client JAR file you'll need to use the API in your app.

While we think this approach makes things really easy, we love to be transparent and we rely on the support of our community to improve the API Client. We love feedback, especially in the form of pull requests. :)

###Download & setup

Download the API Client files in this repo. Here's what you'll find:

The API Client folder contains 2 directories:

1. `API contents/`
2. `DopamineDummy/`

The `DopamineDummy/` direcory contains the *Live Example App*. It's a sandbox example of how one could use the Dopamine Behavior Design API. The *Live Example App* already has the API Client completely integrated and configured and it can be run immediately from your local environment and contact the API. Use it to see how the completed moving parts of the API Client interoperate and to check against your code.

The `API contents/` direcory contains the minimum code to implement the API.

Inside the `API contentes/` directory you'll find:

```
 /
  dopamineAPI.jar
  Initialization calls.png
  Reinforcement call.png
  Tracking Call.png
  dopamine/
    api/
        CustomSSLSocketFactory.java
        CustomX509TrustManager.java
        Dopamine.java
        DopamineRequest.java
        URIBuilder.java
```
<br><br>
##Try it now:
<br>
####Go ahead and try the *Live Example App* now!####
It's preconfigured to run right out of the box and give you a feel for how the different parts of the API Client work together.
<br><br>

We've created this Quick Start guide and the Live Example App to give you a fast way to get the API Client running. We suggest you poke around in the *Live Example App* and see how the different parts of the `Dopamine` object and its methods interact with your app and the API. 

When you're ready to use the `dopamineAPI.jar` JAR file to incorporate the API Client into your app, the rest of this document will help you understand how.
<br>
<hr>
<br>

#Using the API Client
<br>



##What does it take to get running?

Here's how you can get up and running fast:

1. Add the API Client to your project
2. Configure your app and Initialize your app
3. Use the Dashboard to connect user actions with Reinforcement.
4. Send your first tracking call
5. Send your first reinforcement call

<hr>
##Add the API Client to Your Project
<br>
####Import `dopamineAPI.jar` into your project. Give yourself a high five.

<hr>
##Configure your API Client
<br>

####Register a new version on your Dashboard
<br>
The Dopamine API allows you to define many `versions` and `builds` of your app that use the API in different ways, simultaneously. Each `version` and `build` can have different actions you'd like to reinforce, and different Reinforcement Functions to use to reinforce users. There is no limit to the amount of `versions` or `builds` you can have.

Before you configure your code, you need to prep the Dashboard to receive your new `version`:

######Create a new `version` on the Dashboard:
* Log on to your [Developer Dashboard](http://dev.usedopamine.com)
* Go to the [**Design** panel](http://dev.usedopamine.com/design) using the navbar at the top
* On the left-hand `App Versions` panel, click the green **+ create new version** button
* Give your new version a name. This name will correspond to what your will enter in your app. **Make sure these match!**

<br><br>
####Setting your Client Configuration:
<br>
Your `appID`, `key`, and `token` can be found on your Developer Dashboard. NEVER SHARE YOUR TOKEN WITH ANYONE! THIS IS LINKED TO YOUR BILLING INFORMATION! The `versionID` you use should be the same version name you just created on the Dashboard.

<br>
######In your app's onCreate function, add the following code:
<Br>

```
Dopamine.setAppID(<<Your_App_ID>>);
Dopamine.setToken(<<Your_App_Token>>);
Dopamine.setKey(<<Your_App_Key>>);
Dopamine.setVersionID(<<The_Version_Name_You_Created_On_The_Dashboard>>);
```

<br><br>
####Specify your Reinforcement Functions
<br>
The Dopamine API helps your app become habit forming by optimally reinforcing and rewarding your users when they complete a behavior you want to happen more often. Our behavior is shaped by its consequences – especially the positive consequences. But how and when positive consequences happen matters for forming a new habit quickly. Dopamine optimizes the timing and pattern of positive consequences (‘rewards’) and neutral consequences ('feedback') for your users. 

These positive consequences can be parts of the user experience and user interface if your app that deliver a rewarding experience to users. Users respond well to rewards that appeal to their sense of community, their desire for personal gain and accomplishment, and their drive for self-fulfillment. For more information about what makes a great reward and great feedback, check out our blog at http://blog.usedopamine.com.

The functions in your app that deliver rewards to users are called `Reward Functions`. Likewise the functions in your app that deliver neutral feedback to users are called `Feedback Functions`. Collectively the Reward Functions and Feedback Functions in your app are known as your `Reinforcement Functions`. You use the Dopamine API to determine which Reinforcement Function would be encourage optimal habit formation when a user has completed an action you'd like to reinforce.

Your Reinforcement Functions will be pieces of Java code that display either neutral feedback or a reward to users. Here's how to get your Reinforcement Functions registered with the API so we know which functions we can run when your users need to be reinforced:

<br>
#####Create your Reinforcement Functions

Your Reinforcement Functions can be any functions that update the UX to display a positive reward or neutral feedback to the user.

Some of our customers have made Reward Functions that display encouraging messages to the user. Others have included in-app enhancements that get the user excited. Users respond well to rewards that appeal to their sense of community, their desire for personal gain and accomplishment, and their drive for self-fulfillment. For more information about what makes a great reward and great feedback, check out our blog at http://blog.usedopamine.com.

<br>
#####Add your Reinforcement Function names

You need to register your Reinforcement Functions with the API. Use the `Dopamine.addRewardFunction()` and `Dopamine.addFeedbackFunctions()` method of the `Dopamine` object to tell the API the names of the Reinforcement Functions in your app. 

The `addRewardFunction()` and `addFeedbackFunction()` methods each accept one argument. If you have more than one Reward Function or more than one Feedback Function, use the `addRewardFunction()` or `addFeedbackFunction()` method as many times as needed.

<br>

######In your app's onCreate method, after you've configured your client, add:

```
Dopamine.addRewardFunctions(<<The_Name_of_a_Reward_Function_in_your_app>>);
Dopamine.addRewardFunctions(<<The_Name_of_a_Feedback_Function_in_your_app>>);
```


<br>
####Confirm User Identity
<br>
Now that you've created a new `version` on your Dashboard, configured your app with your API credentials, and added your Reinforcement Functions to the `Dopamine` object, your ready to initialize your app. Initializing your app will create a new app `build` on the Dashboard. This `build` will be a property of the `version` you've specified. The Dashboard helps you to specify new user actions to reinforce using your Reinforcement Functions.

App Initialization should happen when a user begins a new session using your app and you can confirm their identity. For many apps, the logical place to perform App Initialization is when a user is logging in to the app. For others, it's when users hit the first screen of the app. 

In Development Mode (when you're making API calls with your Development Key) initialization calls do not result in new user records being specified. In Production Mode (when you're making API calls with your Production Key) initialization calls result in the creation of unique user records. These allow us to optimize reinforcement on a user-to-user basis.

Whenever you've confirmed user identity on your frontend, use the `Dopamine.addIdentity( )` method to set the user's identity with the API.

######In your app's onCreate method, after you've used `Dopamine.addRewardFunction()`, add:

<br>

```
Dopamine.setIdentity(<<IDType>>, <<ID_Value>>);
```
<br>
Here's what Identity types can be used and their associated constraints:

| ID type |   IDType value          |   Example uniqueID    | Notes |
|:---|:--|:----------------------|:---------------|
|User ID # | "userID" |123456789 | Can be any alphanumeric |
|Email Address| "email" | "ramsay@usedopamine.com"|Can be any alphanumeric |
|MAC Address| "mac" | "AB:CD:EF:GH:IJ"|Include colons in address|
|Android ID| "androidID" | "254137A4F0503B16"|[Instructions here](http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id) |
|OAuth Token| "oauth" | "nnch734d00sl2jdk"| |

<br>
######For example:
```
Dopamine.setIdentity("androidID", "254137A4F0503B16");
```
<br>

####...and Initialize your app with the API:
<br>



Initializing your app does 2 things:

* In *Development Mode*, initializing your app confirms your current `build` (the specific set of Reinforcement Functions you're currently using) with the API. The API response will contain a confirmation of the information you uploaded in the initialization call.
* In *Production Mode*, initializing your app checks to make sure this user identity already has a set of reinforcement parameters in the API. If the user identity you submit in the initialization call is new we create a new set of reinforcement parameters for this user.

<br>
######In your app's onCreate method, after you've used `Dopamine.setIdentity()`, add:
```
Dopamine.init(this.getApplicationContext());
```

#####Remember: only call `Dopamine.init()` after you've used `Dopamine.setIdentity()` to establish user identity.


<br>
##Now run your app!
<br><br>
When you initialize your app for the first time, the API will create a new `build` that corresponds with the unique combination of Reinforcement Functions in your app. Any time you use different Reinforcement Functions the API will create a new `build`. If your new `build` contains Reinforcement Functions from a previous `build` the API will try and inherit some of the properties from the old `build`.

<hr>
##Use the Dashboard to connect user actions with Reinforcement.
<br>
Now that your app is initialized and a new `build` has been created, your next step is to associate the actions you'd like to reinforce with the Reinforcement Functions in your app. Actions that you'd like to reinforce can be paired with many Reinforcement Functions, and a given Reinforcement Function can be paired to many actions. Your first step is to use the Dashboard to tell the API the names of your reinforceble actions.
<br><br>
######To create a reinforceble action and pair it to a Reinforcement Function:
<br>
1. Log into your Developer Dashboard and access the **Design** panel in the navbar
2. Click on the name of the `build` you want to work in.
3. Click the green **Add a new reinforced action** button
4. Give your new action a name. This is what you'll use in the code in your app to describe the action with. *Alphanumeric only.*
5. Click the **Associate a new Feedback Function** button to pair your action to a Feedback Function. Select the Feedback Function you want to pair to using the drop-down box. When you've selected the Feedback Function you want, click the **Add this function** button.
6. Repeat step 5 with your Reward Functions.
7. You can optionally add notes that describe this action. These can be information about its trigger and the action itself.
8. When you're ready click the green **Save Changes** button to commit these changes.

Now your new action is paired to your Reinforcement Functions!

<br>

##Tracking your first event

<br>
Dopamine helps you track and understand how people behave inside your app. Anything can be tracked and analyzed. This event could be something as small as a button press or as big as detecting a real-life behavior from your users. To track any event, paste this code snippet in a function that runs when an event happens. 

######Copy/Paste this code into your frontend when you've detected an event you want to track:
<br>
```
Dopamine.track(<<eventName>>);
```
<br>
The argument, `eventName`, is a label you can use to track and analyze how and when this event happens in your app. You can analyze how and when this happens using your Developer Dashboard in the **Analyze** panel.

<br>
##Reinforcing your first behavior
<br>
When you call the API we do some math to determine whether or not a reward or some neutral feedback would be the best way to reinforce your user. Our API response will tell your app which Reinforcement Function to run to optimally reinforce a user. Sometimes we’ll return the name of a Reward Function, sometimes the name of a Feedback Function. Every time it will be optimized to exactly what a user needs. The name of the function we return will always be the name of a Reinforcement Function in your app.

######Copy/Paste this code into your frontend when you've detected an event you want to reinforce:

```
String result = Dopamine.reinforce(<<actionName>>);

if(result.equals(<<The_Name_of_one_of_your_Reinforcement_Functions>>)){
	MainActivity.<<The_Name_of_one_of_your_Reinforcement_Functions>>();
} 
else if(<<The_Name_of_some_other_Reinforcement_Functions>>)){
	MainActivity.<<The_Name_of_some_other_Reinforcement_Functions>>();
}
```

The parameter `actionName` should match an action that you've paired to Reinforcement Functions on your Developer Dashboard. **Make sure these match identically!**

If you want finer-grained control over exactly how each Reinforcement Function runs and what it displays, you can also specify ‘Reward Arguments’ that we can optimize to each user. Reward Arguments are extra details that specify different ways that we can display a reward within a given Reward Function. Reward Arguments are unique to each Reward Function and they’re specified on the developer dashboard. 

We return Reward Arguments in the response.rewardArgument value of the JSON response from the API. You can pass the entire value of response.rewardArgument as an argument to your Reinforcement Function specified inside the appropriate switch case in dopamine_reinforce( ).

