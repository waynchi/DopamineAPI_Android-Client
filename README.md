Dopamine API Android Client
======================

#Quick Start

You can get started with Dopamine in your Android app in just a few minutes. Here’s how:


1.  Download and install this API Client.
2.  Run the Live Example App (DopamineDummy) to immediately get a feel for how to use the API.
3.  Use the Live Example App as a template for how your app can use the Dopamine API.


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

The `API contents/` direcory contains the JAR file as well as the source code for the API.

Inside the `API contentes/` directory you'll find:

```
 /
  dopamineAPI.jar
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

We've created this Quick Start guide and the Live Example App to give you a fast way to get the API Client running. We suggest you poke around in the *Live Example App* and see how the different parts of the `MyDopamine` object and its methods interact with your app and the API. 

When you're ready to use the `dopamineAPI.jar` JAR file to incorporate the API Client into your app, the rest of this document will help you understand how.
<br>
<hr>
<br>

#Using the API Client
<br>



##What does it take to get running?

Here's how you can get up and running fast:

1. Add the API Client to your project
2. Extend the DopamineBase class in which you will configure your app and connect actions with Reinforcement functions
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
######In your project, create a class named `Dopamine` and extend `DopamineBase` from the imported JAR, as shown in the following code:
##### Note: if Eclipse is not recognizing the JAR, make sure you add the file to your build path
<Br>

```
public class Dopamine extends DopamineBase{
    public static void init(Context c){
        // Set Credentials
        appID = "yourAppID";
        versionID = "versionYouCreatedOnDashboard";
        key = "yourKey";
        token = "yourToken";
        
        initBase(c);
    }
}
```

<br><br>
####Specify your Reinforcement Functions
<br>
The Dopamine API helps your app become habit forming by optimally reinforcing and rewarding your users when they complete a behavior you want to happen more often. Our behavior is shaped by its consequences – especially the positive consequences. But how and when positive consequences happen matters for forming a new habit quickly. Dopamine optimizes the timing and pattern of positive consequences (‘rewards’) and neutral consequences ('feedback') for your users. 

These positive consequences can be parts of the user experience and user interface if your app that deliver a rewarding experience to users. Users respond well to rewards that appeal to their sense of community, their desire for personal gain and accomplishment, and their drive for self-fulfillment. For more information about what makes a great reward and great feedback, check out our blog at http://blog.usedopamine.com.

The functions in your app that deliver rewards to users are called `Reward Functions`. Likewise the functions in your app that deliver neutral feedback to users are called `Feedback Functions`. Collectively the Reward Functions and Feedback Functions in your app are known as your `Reinforcement Functions`. You use the Dopamine API to determine which Reinforcement Function would be encourage optimal habit formation when a user has completed a certain action you'd like to reinforce.

Your Reinforcement Functions will be pieces of Java code that display either neutral feedback or a reward to users. Here's how to get your Reinforcement Functions registered with a specific action through the API so we know which functions can run when your users need to be reinforced:

<br>
#####Create your Reinforcement Functions

Your Reinforcement Functions can be any functions that update the UX to display a positive reward or neutral feedback to the user.

Some of our customers have made Reward Functions that display encouraging messages to the user. Others have included in-app enhancements that get the user excited. Users respond well to rewards that appeal to their sense of community, their desire for personal gain and accomplishment, and their drive for self-fulfillment. For more information about what makes a great reward and great feedback, check out our blog at http://blog.usedopamine.com.

<br>
#####Add your Reinforcement Function names and pair them to Actions

You need to register your Reinforcement Functions with the API by linking them to an action. Below is an example of 2 actions that each have two feedback and two reward functions paired with them:
<br>
```
public class Dopamine extends DopamineBase{

// Declare Actions with their names
    public static final DopamineAction action1 = new DopamineAction("action1");
    public static final DopamineAction finishedTask = new DopamineAction("finishedTack");
        
// Declare Feedback Function names
    public static final String FEEDBACKFUNCTION1 = "feedbackFunction1";
    public static final String FEEDBACKFUNCTION2 = "feedbackFunction2";
    public static final String SHOWSCOREHISTORY = "showScoreHistory";
    public static final String SHOWGOALACCOMPLISHED = "showGoalAccomplished";

// Declare Reward Function names
    public static final String REWARDFUNCTION1 = "rewardFunction1";
    public static final String SHOWTROPHY = "showTrophy";
    public static final String GIVEHIGHFIVE = "giveHighFive";
        
    public static void init(Context c){
        // Set Credentials
        appID = "yourAppID";
        versionID = "versionYouCreatedOnDashboard";
        key = "yourKey";
        token = "yourToken";
        
        // Pair Actions to Feedback Functions
        action1.pairFeedback(FEEDBACKFUNCTION1);
        action1.pairFeedback(FEEDBACKFUNCTION2);
        finishedTask.pairFeedback(SHOWSCOREHISTORY);
        finishedTask.pairFeedback(SHOWGOALACCOMPLISHED);
    
        // Pair Actions to Reward Functions
        action1.pairReward(REWARDFUNCTION1);
        action1.pairReward(SHOWTROPHY);
        finishedTask.pairReward(SHOWTROPHY);
        finishedTask.pairReward(GIVEHIGHFIVE);
        
        initBase(c);
    }
```


<br>
####Confirm User Identity
<br>
Now that you've created a new `version` on your Dashboard, configured your app with your API credentials, and added your Reinforcement Functions to the `Dopamine` object, your ready to initialize your app. Initializing your app will create a new app `build` on the Dashboard. This `build` will be a property of the `version` you've specified. The Dashboard helps you to specify new user actions to reinforce using your Reinforcement Functions.

App Initialization should happen when a user begins a new session using your app and you can confirm their identity. For many apps, the logical place to perform App Initialization is when a user is logging in to the app. For others, it's when users hit the first screen of the app. 

In Development Mode (when you're making API calls with your Development Key) initialization calls do not result in new user records being specified. In Production Mode (when you're making API calls with your Production Key) initialization calls result in the creation of unique user records. These allow us to optimize reinforcement on a user-to-user basis.

If you would like to specify your own unique criteria for identity, use the `Dopamine.addIdentity()` method to set the user's identity with the API. You can add as many identity key-value pairs as you want. If you don't want to, that's fine too.
A default feature of the API is that a unique identity generated by hashing the DeviceID, AndroidID, WiFi MAC address, and Bluetooth MAC address together.

######In your custom implementation of the DopamineBase (`Dopamine`), you can add to the `init()` function before `initBase(c)`:

<br>

```
setIdentity("IDtype", "IDvalue");
```
<br>
Here's what Identity types can be used and their associated constraints:

| ID type |   IDType value          |   Example uniqueID    | Notes |
|:---|:--|:----------------------|:---------------|
|User ID # | "userID" |123456789 | Can be any alphanumeric |
|Email Address| "email" | "ramsay@usedopamine.com"|Can be any alphanumeric |
|MAC Address| "mac" | "AB:CD:EF:GH:IJ"|Include colons in address|
|OAuth Token| "oauth" | "nnch734d00sl2jdk"| |

<br>
######For example:
```
Dopamine.setIdentity("email", "JohnDoepamine@puns.com");
```
<br>

####...and Initialize your app with the API:
<br>



Initializing your app does 2 things:

* In *Development Mode*, initializing your app confirms your current `build` (the specific set of Reinforcement Functions you're currently using) with the API. The API response will contain a confirmation of the information you uploaded in the initialization call.
* In *Production Mode*, initializing your app checks to make sure this user identity already has a set of reinforcement parameters in the API. If the user identity you submit in the initialization call is new we create a new set of reinforcement parameters for this user.

<br>
######In your custom Dopamine class, this is performed by the last line in its `init()` function:
```
initBase(c);
```

<br>
######Your custom Dopaine class then needs to be initialized in your app's code, such as in the `onCreate()` method of your main activity, using the line:
```
Dopamine.init( getApplicationContext() );
```

<br>
##Now run your app!
<br><br>
When you initialize your app for the first time, the API will create a new `build` that corresponds with the unique combination of Reinforcement Functions in your app. Any time you use different Reinforcement Functions the API will create a new `build`. If your new `build` contains Reinforcement Functions from a previous `build` the API will try and inherit some of the properties from the old `build`.

<hr>

##Tracking your first event

<br>
Dopamine helps you track and understand how people behave inside your app. Anything can be tracked and analyzed. This event could be something as small as a button press or as big as detecting a real-life behavior from your users. To track any event, paste this code snippet in a function that runs when an event happens. 

######Copy/Paste this code into your frontend when you've detected an event you want to track:

```
Dopamine.track("eventName");
```

The argument, `eventName`, is a label you can use to track and analyze how and when this event happens in your app. You can analyze how and when this happens using your Developer Dashboard in the **Analyze** panel. You can also add metadata to a tracking call as such:
```
Dopamine.addMetaData("dataDescription1", data);  // data can be any type of JSON compatible object. cleared after reinforce()/track()
Dopamine.addPersistentMetaData("dataDescription2", data);  // persistent metadata will be sent with every call
Dopamine.clearPersistentMetaData("dataDescription2");      // clears the persistent metadata
```
The metadata will be sent with any reinforcement or tracking call, and will be cleared once it is sent. Persistent metadata will also be sent with any reinforcement or tracking call, but will not be cleared until clearPersistentMetaData("key") is called.
<br>
<br>
######Tracking calls will be queued if no internet connection is found, and can also be queued to be sent all at once when `sendTrackingCalls()` is called
Whenever an internet connection cannot be made, tracking calls will be queued and logged. Whenever the next tracking call is made, it will be added to the queue. Then a connection will be tried again and the tracking calls will be sent in the order they were added to the queue.
There are 2 available options to give you control over this process. First, you can choose to store tracking calls regardless if a connection can be made or not, and then send them all in one go inside some function or at some specific time.
```
Dopamine.setQuickTrack(false);  // default: true
...
Dopamine.track("event");
if( Dopamine.getTrackingQueueSize()==10 )
    Dopamine.sendTrackingCalls();
```
`getTrackingQueueSize()` returns the number of calls waiting to be sent, and `sendTrackingCalls()` pops calls from the queue and tries to send them off. If a connection fails and there are still elements in the queue, the queue is saved to be tried again when another tracking call is made or when `sendTrackingCalls()` is called.
<br>
<br>
Another form of control you have is whether to keep the tracking calls in memory. The queue is written to a file every time something is added to it or a connection fails. If you choose to send the tracking calls manually, you may choose to store 1000's of calls at once. In order to save some memory, `setMemorySaver()` will remove the queue from memory and instead read it in from the logged file whenever it is needed. Note that this will require a little more processing power per tracking call. `getTrackingQueueSize()` will return the same size regardless of the memorySaver state.
```
Dopamine.setMemorySaver(true);  // default: false
```
<br>
##### Note: These options can be set anywhere in your code at any point in your workflow. If you don't plan on changing these options more than once, we suggest you set the options in your custom `DopamineBase` `init()` function before `initBase(c)`

<br>
##Reinforcing your first behavior
<br>
When you call the API we do some math to determine whether or not a reward or some neutral feedback would be the best way to reinforce your user. Our API response will tell your app which Reinforcement Function to run to optimally reinforce a user. Sometimes we’ll return the name of a Reward Function, sometimes the name of a Feedback Function. Every time it will be optimized to exactly what a user needs. The name of the function we return will always be the name of a Reinforcement Function in your app.

######Copy/Paste this code into your frontend when you've detected an event you want to reinforce:

```
String result = Dopamine.action1.reinforce();

if(result.equals(Dopamine.FEEDBACKFUNCTION1)){
  feedbackFunction1();
} 
else if(result.equals(Dopamine.FEEDBACKFUNCTION2)){
  feedbackFunction2();
}
else if(result.equals(Dopamine.REWARDFUNCTION1)){
  rewardFunction1();
}
else if(result.equals(Dopamine.SHOWTROPHY)){
  showTrophy();
}
```

The `DopamineAction` from your custom `Dopamine` class are public and static, so they can be easily be access from anywhere in your project. The `reinforce()` method of a `DopamineAction` returns the name of the function that should be called in order to optimize the user's reward schedule. The resulting string/function name is also retrievable by `Dopamine.action1.resultFunction`

If you want finer-grained control over exactly how each Reinforcement Function runs and what it displays, you can also use the metadata that was passed when a reinforcement call was made. The metadata can be used in different ways to display a reward within a given Reward Function.

We return the metadata in `action.arguments`. `arguments` is an object array (Object[]), so you will have to cast the object back down to the data it was entered as. Because of this constraint, we suggest remembering in what order data was entered, or creating a custom metadata class.

