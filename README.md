Dopamine API Android Client
======================

#Quick Start

You can get Dopamine in your Android app in just a few minutes. Here’s how:

1. Add the API Client to your project.
2. Add your credtials to the API Client.
3. Send your first initialization call.
4. Send your first tracking call.
5. Define a Reward function.
6. Pair your Reward functoin to an Action.
7. Send your first reinforcement call.

Pro Tip: Check out [the Android demo app](https://github.com/DopamineLabs/DopamineAPI_Android-DemoApp) to see the API in action!

**Let’s get started!**
***

##1) Add the API Client to your project  

**Downlaod the file `dopamineAPI.jar` from this repo. Use your IDE to import it into your project. Give yourself a high five.**

We recomend using the JAR file we've included in this repo to install the Dopamine API Client. The `Dopamine/` floder contains the scourse code for `Dopamine.JAR` and you don't need it for the quickstart.
<!-- How do I import a JAR file? -->
While we think this approach makes things really easy, we love to be transparent and we rely on the support of our community to improve the API Client. We love feedback, especially in the form of pull requests. :)

**Note**: if your IDE is not recognizing the JAR, make sure you add the file to your build path.
<br><br>

##2) Add your credtials to the API Client

In your project, create a class named `Dopamine` and extend `DopamineBase` from the imported JAR like this:
```java
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
Your `appID`, `key`, and `token` can be found on your [Dopamine developer dashboard](http://dev.usedopamine.com/).
The `versionID` can be any string (i.e.'Android v4.3.1', 'iOS Clinical v7.3', 'FINAL VERISION2 FOR REAL THIS TIME', etc).
<br><br>
##3) Send your first initialization call
Initializeation constructs the singleton that you will call for tracking and reinforcing. It also updates the dopamine server with a model of your app. Initialize your custom Dopaine class in the `onCreate()` method of your app's main activity, using the line:
```java
Dopamine.init( getApplicationContext() );
```
**Now run your app!**
<br>
<!-- Do they need any special instructions for where to look to see the http response? Is there some way to read the console from thier laptop? is it ok if they use a vertual device on thier laptop? -->
When you initialize your app for the first time, the API will record that you created a new 'version' and 'build'. Check them out on your [devloper Dashboard](http://dev.usedopamine.com/).
<br><br>

##4) Send your first tracking call

Tracking events help you understand you're users and it helps the Dopamine algortoms learn how to respond to users. To track an event, paste this code so that it will run whenever the event occurs. 

call the `.track` method when you've detected an event you want to track:

```java
Dopamine.track("eventName");
```
The argument, `eventName`, is a label you can use to track and analyze how and when this event happens in your app. 
Try tracking when an button on your apps home screen is tapped. Then run your app and look for the record of the button press on your [devloper Dashboard](http://dev.usedopamine.com/).
<br><br>
##5) Define a Reward function
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
```java
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
##Reinforcing your first behavior
<br>
When you call the API we do some math to determine whether or not a reward or some neutral feedback would be the best way to reinforce your user. Our API response will tell your app which Reinforcement Function to run to optimally reinforce a user. Sometimes we’ll return the name of a Reward Function, sometimes the name of a Feedback Function. Every time it will be optimized to exactly what a user needs. The name of the function we return will always be the name of a Reinforcement Function in your app.

**Copy/Paste this code into your frontend when you've detected an event you want to reinforce:**

```java
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

<hr>

<hr>
#FAQ's

<br>

##What else is in this GIT?

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

The `dopamine/` folder caontains everything that is compiled in the JAR file. You only need the JAR file to use the API Client, but the source is there incase you'd like to take a closer look or customize it.
<br><br>

##Can I see an example app?
Yes! [Our demo Android app](https://github.com/DopamineLabs/DopamineAPI_Android-DemoApp) is preconfigured to run right out of the box and give you a feel for how the different parts of the andoid API Client work together.
<br><br>

##What does the init() call do?
Initializing your app does 2 things:

* In *Development Mode*, initializing your app confirms your current `version` and `build` (the specific set of Reinforcement Functions you're currently using) with the API. The API response will contain a confirmation of the information you uploaded in the initialization call.
* In *Production Mode*, initializing your app checks to make sure this user identity already has a set of reinforcement parameters in the API. If the user identity you submit in the initialization call is new we create a new set of reinforcement parameters for this user.
<br><br>

##What are 'Versions' and 'Builds'?
The Dopamine API allows you to define many `versions` of your app (i.e.'Android v4.3.121', 'iOS Clinical v7.3', 'FINAL VERISION FOR REAL THIS TIME', etc). This way you can pool data across your versions and but still manage them independedtly. A new version is created automatically when you chage the `versionID` varaiable when extending the `DopamineBase` and then make an initialization call. 

If you make a change to the reinforcement functions or thier pairings, the server will generate a new `build` but not a new version. Mutiple builds may be useful during develop, but we strongly recomend that you only release one build per version.

<br>
##Can I Customize User Identity?
App Initialization should happen when a user begins a new session using your app and you can confirm their identity. For many apps, the logical place to perform App Initialization is when a user is logging in to the app. For others, it's when users hit the first screen of the app. 

In Development Mode (when you're making API calls with your Development Key) initialization calls do not result in new user records being specified. In Production Mode (when you're making API calls with your Production Key) initialization calls result in the creation of unique user records. These allow us to optimize reinforcement on a user-to-user basis.

If you would like to specify your own unique criteria for identity, use the `Dopamine.addIdentity()` method to set the user's identity with the API. You can add as many identity key-value pairs as you want. If you don't want to, that's fine too.
A default feature of the API is that a unique identity generated by hashing the DeviceID, AndroidID, WiFi MAC address, and Bluetooth MAC address together.

n your custom implementation of the DopamineBase (`Dopamine`), you can add to the `init()` function before `initBase(c)`:
```java
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
For example:
```java
Dopamine.setIdentity("email", "JohnDoepamine@puns.com");
```
<br>

##Can I atach metadata to Tracking and Reinforcement calls?

like `'eventName'`, you can use metadata to analyse Tracking and Reinforcement calls.  Here's how to add metadata:
```java
Dopamine.addMetaData("dataDescription1", data);  // data can be any type of JSON compatible object. cleared after reinforce()/track()
Dopamine.addPersistentMetaData("dataDescription2", data);  // persistent metadata will be sent with every call
Dopamine.clearPersistentMetaData("dataDescription2");      // clears the persistent metadata
```
The metadata will be attached to the next reinforcement or tracking call, and then cleared. Persistent metadata will also be sent with any reinforcement or tracking call, but must be cleared manually using:
```java
clearPersistentMetaData("key");
```
<br>

##How does Asychrynous Tracking work?
Sometimes your users doesn't have internet access and API calls can't get out. Asynchrynous tracking makes sure that those calls are stored and sent the next time they do get a connecton.


####controling when calls are delivered
Whenever an internet connection cannot be made, tracking calls will be queued and logged. The Dopamine client will atempt to send the queued calls every time an `init()`, `track()` or `reinfore()` function is called. You can also force it to try and deliver the queued calls with `sendTrackingCalls()`.


####settings for asychrynous tracking
By default, calls only go in to the que if they cannot be sent imediatly. You can turn this feature off with this command:
```java
Dopamine.setQuickTrack(false);  // default: true
```
But then you have to manually send the calls. Using `Dopamine.sendTrackingCalls()`. For example, this would clear the log whenever it contained more then 10 items:
```java
Dopamine.track("event");
if( Dopamine.getTrackingQueueSize()>10 )
    Dopamine.sendTrackingCalls();
```
`getTrackingQueueSize()` returns the number of calls waiting to be sent. If a connection fails and there are still elements in the queue, the queue is saved to be tried again when another tracking call is made or when `sendTrackingCalls()` is called.
<br>
<br>
Also, by default the client saves the call que in two places. It stores it in the voletile memory (for speed) and it writes it to a file (for persistance). If you choose to send the tracking calls manually, you may choose to store 1000's of calls at once. In order to save some memory, `setMemorySaver()` will remove the queue from memory and instead read it in from the logged file whenever it is needed. Note that this will require a little more processing power per tracking call. `getTrackingQueueSize()` will return the same size regardless of the memorySaver state.
```java
Dopamine.setMemorySaver(true);  // default: false
```
**Note**: These options can be set anywhere in your code at any point in your workflow. If you don't plan on changing these options more than once, we suggest you set the options in your custom `DopamineBase` extending `init()` function before `initBase(c)`.

##What should I track?

<br><br>
##What?! A Singleton?
We think that this client is a great way to handle tracking and reinforcement throughout an Android aplication. But we also konw that many people [have strong feelings about singletons](http://stackoverflow.com/questions/137975/what-is-so-bad-about-singletons). You can access the Dopamine API with a custom client. The REST-like API at api.dopamine.com will record and answer any properly formated request. It will also answer improperly formatted requests with a verbose error. Email us if you want any help building your own client team-[at]-usedopamine.com. 