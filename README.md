Dopamine API Android Client
======================

#Quick Start

You can get Dopamine in your Android app in just a few minutes. Here’s how:

1. [Initialize the API Client and run your app.](https://github.com/DopamineLabs/DopamineAPI_Android-Client#1-initialize-the-api-client-and-run-your-app)
2. [Track your first event](https://github.com/DopamineLabs/DopamineAPI_Android-Client#2-track-your-first-event)
3. [Reinforce your first action](https://github.com/DopamineLabs/DopamineAPI_Android-Client#3-reinforce-your-first-action)

Pro Tip: Check out [the Android demo app](https://github.com/DopamineLabs/DopamineAPI_Android-DemoApp) to see the API in action!

**Let’s get started!**
***
#1) Initialize the API Client and run your app.
##1.1) Add the API Client to your project  

**Downlaod the file `dopamineAPI.jar` from this repo. Use your IDE to import it into your project. Give yourself a high five.**

if your IDE is not recognizing the JAR, make sure you add the file to your build path.
<br><br>

##1.2) Add your credtials to the API Client
Now you neep to let the client konw a litle bit about who you are and what you're working on. To do this, create a class named `Dopamine` to extend `DopamineBase` from the imported JAR.
**Copy&Paste this in to your project:**
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
##1.3) Initialize the client and run your app
In the last step, you created the `Dopamine.init()` method. You need to call that method as soon as the app boots to make the tracking and reinforcement methods available to the rest of the app. 
**Copy&Paste this line into the `onCreate()` method of your app's main activity:**
```java
Dopamine.init( getApplicationContext() );
```
<br>
##1.4) Now run your app!
<!-- Do they need any special instructions for where to look to see the http response? Is there some way to read the console from thier laptop? is it ok if they use a vertual device on thier laptop? -->
When you initialize your app for the first time, the API will record that you created a new 'version' and 'build'. Check them out on your [devloper Dashboard](http://dev.usedopamine.com/).
<br><br>
<hr>
#2) Track your first event
##2.1) Place the tracking code
Tracking events help you understand you're users and it helps the Dopamine algortoms learn how to respond to users. 
**To track an event, paste this code so that it will run whenever the event occurs.**
```java
Dopamine.track("eventName");
```
The argument, `eventName`, is a label you can use to track and analyze how and when this event happens in your app. 
Try tracking when an button on your apps home screen is tapped. 
<br><br>
##2.2) Now Run your app!
Run your app and trigger the event tracking code you placed. Look for the record of the button press on your [devloper Dashboard](http://dev.usedopamine.com/).
<br><br>
<hr>
#3) Reinforce your first action
If a user is rewared for an action, they are more likely to reapeat that action. But if you reward them everytime, they quickly learn to ignore it. So when and how should you reward them and when should you just give them feedback?
After a user completes a target action, use the `.reinforce()` method to ask if now is the right time to reward a user. Let's prepare your project so that you can use `.reinforce()` in your app.
<br>
##3.1) Tell the client about your reinforcement functions
When the API desides that it's the right time to reward a user for completing your tagerget action, it triger one of your "reinforcement fucntions". A "reward functions" when it's the right time to reward a user or a "feedback functions" when it desides not to reward the user. A reward function can be anything(app enhancement, notification, animation) that makes the user feel good. A feedback function dryly inform a user that they completed the action. These can be subtle (a button changes color, a screen advances to a summary screen) or overt (dice roll going poorly, slot machine wheel loosing). You don't need to write the functions yet. Just name them and let the client know that they exist. For now, make your reward a notification about what an awesome developer you are, and make your feedback a notification that the server heard your request.

Declair the names of the reinforcement fucntions in your code from step 1.2 so that it looks like this:

```java
public class Dopamine extends DopamineBase{

// Declare Feedback Function names
    public static final String SERVERHEARDYOU = "SeverHeardYou";

// Declare Reward Function names
    public static final String YOUAREAWESOME = "YouAreAwesome";
        
    public static void init(Context c){             //  \
        // Set Credentials                          //   |
        appID = "yourAppID";                        //   |--This should already be 
        versionID = "versionYouCreatedOnDashboard"; //   |--in your project.
        key = "yourKey";                            //   |
        token = "yourToken";                        //   |
                                                    //   |
        initBase(c);                                //  /
    }
```
<br><br>
##3.2) Tell the client about your target action
Now we need to name the action that you want to reinforce and tell the client about it. For now, lets name it DEVELOPERTEST.

**Paste in an action decairation so that your extention looks like this:**
```java
public class Dopamine extends DopamineBase{

// Declare Actions with their names
    public static final DopamineAction DEVELOPERTEST = new DopamineAction("DevTest");

// Declare Feedback Function names                                                      //   \
    public static final String SERVERHEARDYOU = "ServerHeardYou";                       //   |
                                                                                        //   |
// Declare Reward Function names                                                        //   |
    public static final String YOUAREAWESOME = "YouAreAwesome";                         //   |
                                                                                        //   |
    public static void init(Context c){                                                 //   |
        // Set Credentials                                                              //   |
        appID = "yourAppID";                                                            //   |--This should already be 
        versionID = "versionYouCreatedOnDashboard";                                     //   |--in your project.
        key = "yourKey";                                                                //   |
        token = "yourToken";                                                            //   |
                                                                                        //   |
        initBase(c);                                                                    //  /
    }
```

<br><br>
##3.3) Connect an action to your reinforcement function
YOu can have lots of actions, reward functions, and feedback functions. You need to let the client know which reward/feedback fucntions are aproriet to which actions. You do this by decairing "pairings". Lets pair your action to the two reward/feedback fucntions you decaired.

```java
public class Dopamine extends DopamineBase{

// Declare Actions with their names
    public static final DopamineAction DEVELOPERTEST = new DopamineAction("DevTest");   //   \
                                                                                        //   |
// Declare Feedback Function names                                                      //   |
    public static final String SERVERHEARDYOU = "ServerHeardYou";                       //   |
                                                                                        //   |
// Declare Reward Function names                                                        //   |
    public static final String YOUAREAWESOME = "YouAreAwesome";                         //   |
                                                                                        //   |
    public static void init(Context c){                                                 //   |
        // Set Credentials                                                              //   |
        appID = "yourAppID";                                                            //   |--This should already be 
        versionID = "versionYouCreatedOnDashboard";                                     //   |--in your project.
        key = "yourKey";                                                                //   |
        token = "yourToken";                                                            //   /

        // Pair Actions to Reinforcement Functions
        DEVELOPERTEST.pairFeedback(SERVERHEARDYOU);
        DEVELOPERTEST.pairReward(YOUAREAWESOME);

        initBase(c); 
    }
```

<br><br>
##3.5) Place your action logic
Nowe we need to write the reinforcment fuctions that we told the client about and plave the code in where it will run when the target action is completed. For this quick start, pick a target action that you can quickly trigger multiple times. We'd recomend a button on your home screen. 


**paste this code so that it will run whenever a user completes your target action:** 
```java

public void youAreAwesome( ){
    Toast.makeText(this, "You Are Awesome!!", Toast.LENGTH_SHORT).show();
}

public void ServerHeardYou( ){
    Toast.makeText(context, "The Server received your call", Toast.LENGTH_SHORT).show();
}

String result = Dopamine.DEVELOPERTEST.reinforce();

if(result.equals(Dopamine.YOUAREAWESOME)){
  youAreAwesome();
} 
else if(result.equals(Dopamine.SERVERHEARDYOU)){
  serverHeardYou();
}
```
During initialization, the client creates the object `Dopamine.DEVELOPERTEST` and gives it the method `.reinforce()`. The client also tells the server what reinforcement fucntions were paired to the `DEVELOPERTEST` action, so it knows what responses are valid. 
<br><br>
##3.4) Run your app

Boot your app and trigger the target action code a few times. Sometimes you'll get the feedback and sometimes you'll get the reward. For now, that the feedback/reward are triggered at random. After you get your production key your users will be getting personalized schedules to drive engagement and retention.


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

##Can I attach metadata to Tracking and Reinforcement calls?

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
<br><br>
##Can I provide input arguements to the reward functions?

<br><br>
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
Also, by default the client saves the call que in two places. It stores it in the voletile memory (for speed) and it writes it to a file (for persistance). If you choose to send the tracking calls manually, you may choose to store 1000's of calls at once. In order to save some memory, `setMemorySaver()` will remove the queue from memory and instead read it in from the logged file whenever it is needed. Note that this will require a little more processing power per tracking call. `getTrackingQueueSize()` will return the same size regardless of the memorySaver state.
```java
Dopamine.setMemorySaver(true);  // default: false
```
**Note**: These options can be set anywhere in your code at any point in your workflow. If you don't plan on changing these options more than once, we suggest you set the options in your custom `DopamineBase` extending `init()` function before `initBase(c)`.

##What should I track?
<br><br>
##What?! A Singleton?
We think that this client is a great way to handle tracking and reinforcement throughout an Android aplication. But we also konw that many people [have strong feelings about singletons](http://stackoverflow.com/questions/137975/what-is-so-bad-about-singletons). You can access the Dopamine API with a custom client. The REST-like API at api.dopamine.com will record and answer any properly formated request. It will also answer improperly formatted requests with a verbose error. Email us if you want any help building your own client team-[at]-usedopamine.com. 

