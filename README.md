Dopamine API Android Client
======================

#Quick Start

You can get started with Dopamine in your Android app in just a few minutes. Here’s how:

1. Add the API Client to your project.
2. Add your credtials to the API Client.
3. Send your first initialization call.
4. Send your first tracking call.
5. Define a Reward function.
6. Pair your Reward functoin to an Action.
7. Send your first reinforcement call.

Pro Tip: Check out [the Android demo app](https://github.com/DopamineLabs/DopamineAPI_Android-DemoApp) to see the API in action!

**Let’s get started!**

<hr>

##Add the API Client to Your Project

<br>
####Import ```dopamineAPI.jar``` into your project. Give yourself a high five.

Our recommended way of installing the Dopamine API Client is to use the JAR file we've included in this repo. The ```Dopamine/``` floder contains the scourse code for ```Dopamine.JAR``` and you don't need it for the quickstart.
<!-- How do I import a JAR file? -->
While we think this approach makes things really easy, we love to be transparent and we rely on the support of our community to improve the API Client. We love feedback, especially in the form of pull requests. :)

##### Note: if Eclipse is not recognizing the JAR, make sure you add the file to your build path
<br><br>
<hr>
<br>
##Configure your API Client
<br>
######In your project, create a class named `Dopamine` and extend `DopamineBase` from the imported JAR, feel free to copy&paste the following code:

<Br>

```
public class Dopamine extends DopamineBase{
    public static void init(Context c){
        // Set Credentials
        appID = "yourAppID";
        versionID = "AnyStringForThisVersion";
        key = "yourKey";
        token = "yourToken";
        
        initBase(c);
    }
}
```
<br>
Your `appID`, `key`, and `token` can be found on your [Dopamine developer dashboard](http://dev.usedopamine.com/) . NEVER SHARE YOUR TOKEN WITH ANYONE! 
The `versionID` can be any string (i.e.'Android v4.3.121', 'iOS Clinical v7.3', 'FINAL VERISION FOR REAL THIS TIME', etc).
<br><br>


##Initialize your app with the API
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
if( Dopamine.getTrackingQueueSize()>10 )
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




Here's whats in the GIT:

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

The ```dopamine/``` folder caontains everything that is compiled in the JAR file. You only need the JAR file to use the API Client, but the source is there incase you'd like to take a closer look or customize it.  
<br><br>
##See it in action:## [Our demo app](https://github.com/DopamineLabs/DopamineAPI_Android-DemoApp) is preconfigured to run right out of the box and give you a feel for how the different parts of the API Client work together.


##Why do I need to use the init() call?
<br>
Initializing your app does 2 things:

* In *Development Mode*, initializing your app confirms your current `version` and `build` (the specific set of Reinforcement Functions you're currently using) with the API. The API response will contain a confirmation of the information you uploaded in the initialization call.
* In *Production Mode*, initializing your app checks to make sure this user identity already has a set of reinforcement parameters in the API. If the user identity you submit in the initialization call is new we create a new set of reinforcement parameters for this user.

<br>


#Versions and Builds

####Register a new version on your Dashboard
<br>
The Dopamine API allows you to define many `versions` of your app (i.e.'Android v4.3.121', 'iOS Clinical v7.3', 'FINAL VERISION FOR REAL THIS TIME', etc). This way you can pool data across your versions and but still manage them independedtly. A new version is created automatically when you chage the `versionID` varaiable when extending the `DopamineBase` and then make an initialization call. 

If you make a change to the reinforcement functions or thier pairings, the server will generate a new `build` but not a new version. Mutiple builds may be useful during develop, but we strongly recomend that you only release one build per version.


