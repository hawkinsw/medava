## Medava

Java is an amazing language. The original designers, James Gosling, Bill Joy and Guy Steele, described it as, "... a general-purpose concurrent class-based object-oriented programming language" (sic). By now, as programming-language enthusiasts, you should recognize those words. In particular, we are focused here on the fact that they describe Java as a class-based (ie, *not* prototypal), object-oriented programming language. By virtue of its support for OOP and the criteria we set for such a language to be considered such a language, we can assume that Java supports

1. The _abstraction_ of abstract data types (ADTs),
2. Inheritance
3. Virtual methods (*and* open recursion).

In this mini assignment we will explore how Java meets each of these criteria and learn how Java allows the programmer to employ reflection, another feature often present in object-oriented programming languages.

As for its support for typing, the language specification says it well:

> The Java programming language is a statically typed language, which means that every variable and every expression has a type that is known at compile time.

Or,

> The Java programming language is also a strongly typed language, because types limit the values that a variable ... can hold or that an expression can produce, limit the operations supported on those values, and determine the meaning of the operations. Strong static typing helps detect errors at compile time.

And yet Java is known for its dynamism. This mini assignment will also explore the tension that exists in Java as a result -- the tension between the language's dynamism and its strong-typing constraints.


## Java is *Not* JavaScript

To start, let's be very, very clear: Java is *not at all* related to JavaScript. JavaScript is a language that is specified through a group in the EU formerly known through the acronym ECMA which stood for European Computer Manufacturers Association. In 1994, the organization dropped its primary focus on Europe and became an international body. It became officially known as Ecma, which is *not* an acronym (in the same way that [KFC and Kentucky Fried Chicken may or may not be the same thing](https://www.snopes.com/fact-check/kfc-and-fried/)). The group's charter is to standardize information and computer systems. (This information is derived from the [Ecma website](https://www.ecma-international.org/about-ecma/history/)) As such, it is more accurate to refer to JavaScript as [ECMAScript](https://tc39.es/ecma262/).

The original developer of JavaScript (Brandon Eich) was working for Netscape when he created the language and he is quick to remind everyone that the name JavaScript was just a ["marketing scam"](https://www.youtube.com/watch?v=WqMbzVWIAjY). The original code name for the language was Mocha and the second name was LiveScript (yes, I wrote code in LiveScript!). Brendan recalls that the goal for "Marc (Andreeson, founder of Netscape) and Bill (Joy, one of the founders of Sun Microsystems and original developer of Vi) was to make [JavaScript] the sidekick language to Java" [ref](https://www.youtube.com/watch?v=WqMbzVWIAjY).

In other words, beyond marketing and hype, Java and ECMAScript, nee JavaScript, have nothing to do with one another and are completely independent languages.

## Prerequisites:

First things first: Clone this repository to your development machine! You are all experts at this by now, I am sure, but there are great resources online for learning/using `git` if you want to dig deeper: [Resource](https://git-scm.com/download/mac), [Resource](https://gitforwindows.org/), [Resource](https://git-scm.com/docs/gittutorial).

Next, you will need a Java SDK and Maven to complete this assignment. I _highly_ recommend that you use VS Code (whether you are on Windows, macOS or Linux). There are instructions for configuring the IDE on those platforms [online](https://code.visualstudio.com/docs/java/java-build). If you choose not to use VS Code, there is plenty of documentation for configuring [your](https://docs.oracle.com/en/java/javase/15/install/installation-jdk-macos.html) [environment](https://maven.apache.org/install.html) [online](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html).

A system development kit (SDK) contains the pieces that you need to develop in a particular toolchain, in this case Java. The Java SDK is known as the Java Development Kit (the JDK). The JDK gives you the ability to _write_ and _compile_ Java code. However, it does _not_ give you the ability to _execute_ Java code. The tool(s) needed to execute Java programs are part of toolkits known as Java Runtime Environments (JREs). Those tools are "sold separately", as they say. What do you think might be the reason why those two groups of tools would be distributed separately?

Away we go!

## The Situation

In this mini assignment we are going to write an application that simulates a supply chain. We will build the application first to transport medicines between their manufacturer and hospitals and then slowly expand it (through generalization) to support simulations of different types of supply chains. The entire application is started and controlled by a `main` function (it's technically a method and we will return to the distinction between methods and functions below) in the `SupplyChain.java` file in the `src` directory. Like C and C++ programs, all Java applications start with a so-called `main`. Like the main functions in C and C++ programs, the `main` "function" in Java also has to have a very particular signature:

```Java

public static void main(String args[]) {
  ...
}
```

If you are familiar with the signature of the `main` function in C and C++, nothing about this declaration will be surprising. The signature is defining a method (technically a `static` method) named `main` that takes an array (of variable length) of `String`s as a parameter. That array is filled with the values that are given on the _command line_ when the program is executed. For the Medava application, we do not accept any input from the user when they execute the program so we will not deal with the `args` parameter any further. Even though our application will not take any input from the user on the command line, we *must* keep that parameter. (*Note*: In C/C++, the `main` function also includes parameters where values from the command line are made accessible to be programmer. However, in C/C++, naming that parameter is optional.)

What do the other components of the declaration indicate? There's a `static` and a `public`. We will talk about `public` later so let's focus on `static` here. Before doing that, we have to see more context around the `main` function. Notice that the `main` function is actually in a class! Why? Because everything in Java has to be in a class. In that sense, Java can be called a pure OOP language. Unlike C++ where standalone functions are allowed (because it is a hybrid between imperative and object-oriented), everything in Java must be contained in a class.

If everything is a class, then all the subprograms that we define in Java are actually methods (which is not only the academic term of art in object-oriented programming for procedures that operate on the data in an instance of an abstract data type (Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). Design patterns: Elements of reusable object-oriented software. Addison Wesley.) but it is also the language-specific term that Java uses). Based on the definition of a method, we would expect that in order to execute one, we must have instantiated an object of the type in which that method is declared (otherwise, that method would not have any data on which to execute)! Normally, yes, that would be the case. However, using the `static` keyword modifier on a method makes that method a static method and gives the programmer the power to be able to invoke that method without having to instantiate its containing class. In this case, we do not have to instantiate a `SupplyChain` class to be able to call `main`.

The syntax for invoking a non-`static` method requires an instance of the class:

```Java

MyDataType mdt = new MyDataType();
mdt.someMethod();
```
Whereas invoking a static method (`someStaticMethod`) does not require an instance:

```Java
MyDataType.someStaticMethod();
```

As a result, `someStaticMethod` cannot make reference to any of the member variables (what Java calls _fields_) of the `MyDataType` class. Can you understand why? In an OOP, each instance of a class has its own, private copy of all the member variables whose values are completely independent of the values of the member variables of any other instance. By invoking a method on a particular instance (`mdt` in the example above), the caller provides the method the context it needs to find those values. When the programmer calls a `static` method, the method gets no such context. It is, therefore, nonsensical for a `static` method to even think about member variables. It might make more sense if you use an alternate name for member variables: [_instance_ variables.](https://en.wikipedia.org/wiki/Instance_variable)

Because the `SupplyChain` class contains a `public`, `static` method named `main` with an appropriate set of parameters and return type , and that is the only class that we will compile that contains such a method, the Java _virtual machine_ (JVM) will automatically invoke this method when the program is launched. After launching that method, the JVM will shepherd along program execution in whatever way the program's code defines.

To witness this process in action (and confirm that your installation and configuration of Java is correct), add

```Java
System.out.println("Hello, <your name>! This is the WOPR speaking.");
```

to the body of the `main` function in the `SupplyChain` class and then execute the program. Once you have confirmed that the program runs (by checking that the program produces

```
Hello, Will! This is the WOPR speaking.
```

as output), you can comment out that line of code. _Do not remove it_.

## What Goes Around Comes Around

The pharmacy creates medicine and needs to ship it to the hospital. In the simulation, we are not concerned about the pharmacology of the medicine (per se) but we are concerned about how its chemical makeup might affect its transportation. For instance, does the medicine's composition require that it be refrigerated during transportation? That it cannot be jostled? That it cannot be in transport for longer than a certain period of time?

Let's start building a `Medicine` class. The `Medicine` class will be the _base class_ (what Java calls a _superclass_) for all the medicines that are transportable between point A and point B in our simulation. We will add functionality to the class (and its subclasses [what Java also calls subclasses]) throughout the assignment, but there are a few things that we can configure upfront.

When the programmer _instantiates_ (creates an instance of [unhelpfully]) a class (by using the `new` keyword to write a _[class instance creation expression](https://docs.oracle.com/javase/specs/jls/se18/html/jls-15.html#jls-15.9)_), the class' _constructor_ is called. The job of the constructor is to perform any actions that are required to initialize an instance of that particular type (e.g., initialize instance variables, configure external resource access [database connections, file handles, network connections, etc]). The constructor is like any other method in that it may have parameters. However, the constructor differs from other methods in that its name is prescribed by the language (it must be the same as the name of the class) and it does not actually return anything and, therefore, has no return type[^1]. The constructor for our `Medicine` class will take a single parameter -- the name of the medicine (as a `String`).

[^1]: That probably reminds you of the requirements for constructors in C++!

In the `Medicine` class in the `Medicine.java` file, write the following constructor:

```Java

public Medicine(String medicineName) {

}
```

That code defines a constructor that takes a parameter whose name is `medicineName` and has the type `String`. We want to store that user-supplied medicine name as a field so that our methods of the `Medicine` class can use it. To do that, we will need to create that field. In the `Medicine` class, declare the `mMedicineName` field of type `String` by writing:

```Java
private String mMedicineName;
```

somewhere _in_ the class but _outside_ the constructor. In the constructor, we have to set our new field's value to the user-supplied value. Add the following line to the constructor:

```Java
mMedicineName = medicineName;
```

The declaration statement declares a field named `mMedicineName` whose type is `String` and specifies that it has `private` access level. Access control in an OOPL is a tool for the programmer to specify who is allowed to access an entity. Generally, access is granted/revoked according to different audiences:

1. *clients*: Clients are other programmers who use a class by instantiating it.
2. *derivers*: Derivers are other programmers who modify/extend a class by inheriting from it (see below).
3. *self*: Self is the programmer who is implementing the class (i.e., us!).

The _[access modifiers](https://docs.oracle.com/javase/specs/jls/se18/html/jls-6.html#jls-6.6)_ that Java provides correspond to those three audiences:

1. `public`: _anyone_ can access it (in particular, the _client_ can access it).
2. `protected`: generally speaking (there are caveats and exceptions, of course), a _protected_ entity can only be accessed the programmer implementing the class or a deriver.
3. `private`: A _private_ entity can only be accessed by itself (again, with exceptions).


Because `mMedicineName` is declared to be private, none of our clients (or even subclasses) can get access to it directly. How, then, will they use it? We should give them a means to read that value. We will write a _getter_ -- a method that allows external entities a view of a private entity. In the `Medicine` class, write the following getter:

```Java
public String getMedicineName() {
  return mMedicineName;
}
```

The method `getMedicineName` will return to the caller a copy of the name of the medicine. We specified that clients (and derivers) can invoke it by giving it the `public` access modifier. Great. That was easy.

The only other method that we want to implement now is a method that will return `true` or `false` depending on whether the medicine remains safe if subjected to a particular temperature range. Given two temperatures, a low temperature and a high temperature, the method will return `true` if the medicine is safe at a temperature in that range and returns `false` if it would become unsafe at any temperature in that range. Write the following method in the `Medicine` class to accomplish this functionality:

```Java
    public boolean isTemperatureRangeAcceptable(Double lowTemperature, Double highTemperature) {
        if (this.minimumTemperature() <= lowTemperature &&
                highTemperature <= this.maximumTemperature()) {
            return true;
        }
        return false;
    }
```

`boolean` is the keyword for a Boolean value in Java and `Double` specifies a variable whose type is a non-primitive, double-precision floating-point value (more on the difference between a primitive and non-primitive floating-point value below).

So, what of `this.minimumTemperature()` and `this.maximumTemperature()`? Will, we never declared an instance named `this` and you said that methods all required instances, so what gives? In Java, `this` is a special keyword [that](https://docs.oracle.com/javase/specs/jls/se18/html/jls-15.html#jls-15.8.3)

> ... denotes a value that is a reference to the object for which the instance method was invoked ..., or to the object being constructed.

That helps! So, we know that the current method invocation (on the `isTemperatureRangeAcceptable` method) and the code in those two method invocations will have access to fields of the same instance of the `Medicine` class via the `this` instance variable.

It also gives us a task: we must implement those methods so that the `isTemperatureRangeAcceptable` method can use them. Obviously not every medicine has the same range of safe temperatures. However, it seems clear that there is a baseline reasonable range of temperatures outside of which our medicine will spoil. In the `Medicine` class (what will eventually become a superclass), then, we will provide a so-called *default* implementation of the two methods and we will leave it up to any subclasses to _override_ these methods if they want to customize functionality specific to a type of medicine. In other words, our software will rely on the support for _virtual methods_ in Java to increase programmer efficiency! Type in the following method declarations of the `Medicine` class in `Medicine.java`:

```Java

    public double minimumTemperature() {
        return 0.0;
    }

    public double maximumTemperature() {
        return 100.0;
    }
```

That's a good start for the implementation of our `Medicine` class. Let's move on to an implementation of the class that will actually simulate the thing that does the transportation!

You may be asking yourselves at this point, "Will, why are we going through the hassle of defining methods for the maximum and minimum temperature? Why can't we just make those fields (or what are called instance variables in some other languages)?" Well, we _could_ do that, but we would lose a ton of power! If a subclass of our `Medicine` declared (later) a different minimum and maximum temperature field (in order to override the defaults provided by the super class), any method implemented in the superclass that attempts to access that field (instance variable) _will access the value of the field in the super class_ and _not_ the value in the actual class. In other words, field access does not follow open recursion! See the `java_late_binding` demo in the example code repository for this class for a more complete explanation!

## Eastbound and Down

All good things have names (and, apparently, come to those who wait). Let's set up the transporter that we are going to use to move goods so that it can have a name. Add the following code to the `Transporter` class in `Transporter.java`:

```Java

    private String mTransporterName;
    public String getTransporterName() {
        return mTransporterName;
    }
```

Whether it is a semitruck, train or plane, a thing that transports goods contains a bunch of those goods. So, we know that our class that represents a transporter will at least need to have some way to store the goods that it is responsible for moving. Add the following field declaration to the `Transporter` class in `Transporter.java`:

```Java

private List<Medicine> goods;
```

We have seen most of those syntactic elements before, but there is one new thing. `List` is a _generic type_ in Java. Generic types are the Java-specific way of building _parametric ADTs_. This declaration instructs Java that `goods` will be a `List` of `Medicine`s. `goods` will only support storage of instances of the `Medicine` class, and, crucially, any of its subclasses!

The declaration here creates a variable of _reference type_. A reference type in Java is _the_ way to refer to an instance of a user-defined ADT (in other words, a class). Java's standard library is kind enough to define the `List` type for us, but our work is not done.

First, we have to tell Java where to find the implementation of the `List` class. Java has the `import` statement that works like the `#include` preprocessor directive in C++. Import the `List` type by adding this `import` statement to the beginning of the `Transporter.java` file:

```Java
import java.util.List;
```

Second, the initial value of a reference type variable (i.e., any variable that has a type other than a primitive) is `null`. So, we have a problem -- we cannot leave the value of `goods` as `null` for very long or else we run the risk of attempting to use a variable whose value is `null`. What are we to do?

Java offers an awesome opportunity to initialize fields when an instance is created. No, its not the constructor (although we could always do it there). Rather, if there is an unnamed block of code (ie, code between a `{` and `}`) contained in the class declaration, the code in that block executes every time that an instance of the class is created. Java calls these blocks *[instance initializers](https://docs.oracle.com/javase/specs/jls/se18/html/jls-8.html#jls-8.6)*. No matter which constructor is invoked (constructors can be overloaded in Java which means that there can be multiple constructors per class!), the code in instance initializers is guaranteed to be executed *after* the constructor! Woah!

Add the following instance initializer to the `Transporter` class:

```Java
{
  goods = new List<Medicine>();
}
```

Uhoh! What do we see? Java is telling us that we cannot instantiate a variable whose type is `List`. Why not? That's because `List` in Java is an _[abstract class](https://docs.oracle.com/javase/specs/jls/se18/html/jls-8.html#jls-8.1.1.1)_. An abstract class is a class in Java that specifies a set of supported methods but does not offer even a baseline implementation for (at least one of) those methods. You can only instantiate _subclasses_ of abstract classes that implement the abstract class' methods. Java offers several different subclasses of `List` that we can choose from. To reiterate, no matter what subclass we choose, it will support the same operations as the `List` and will meet Java's technical requirements to be a [_subtype_](https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.10)[^2] of a `List`. We will, rather arbitrarily, choose the `ArrayList`. Replace the code in the instance initializer with

[^2]: Does Java's definition of a subtype match with the definition from Barbara Liskov that we learned in class?

```Java
{
  goods = new ArrayList<Medicine>();
}
```
Like the `List` itself, we have to tell Java where to find the implementation of `ArrayList`:

```Java
import java.util.ArrayList;
```

Now we are getting somewhere.

As for behaviors, it makes sense to give a user of the transporter the ability to load it and unload it and the ability to use it for shipping! Loading and unloading the transporter involves simply updating the contents of the `goods` field. So, we'll just modify the access specifier for that field and set it to `public`. That way our clients can add and remove (load and unload) goods as they see fit.

```Java

public List<Medicine> goods;
```

Next, let's add a function that simulates the shipping of the goods via the transporter. We'll call it `ship` and it won't return a value:

```Java
public void ship() {
  // Do some shipping!
}
```

We can add functionality later, but for now leaving the `ship` method without a meaningful body is okay.

We have things to ship (`Medicine`s) and we have a way to ship them (`Transporter`). Let's make a few entities that will actually use the services provided by those classes.

## What Goes Around Comes Around

The source of our shipments is going to be the pharmacy where the drugs are prepared. The destination of our shipments will be the hospital where the drugs heal the sick. The two classes that we will use to simulate this sending and receiving process will need to be able to load and unload, respectively, a transporter. Each of the two classes will also have a name so that we can more easily identify each when the simulation is running. Add the following code to `Hospital.java`:

```Java
public class Hospital {

    public Hospital(String hospitalName) {
        name = hospitalName;
    }

    void receive(Transporter t) {
        while (!t.goods.isEmpty()) {
            Medicine unloaded = t.goods.remove(0);
            System.out.println(String.format("Receiving an %s.", unloaded.getMedicineName()));
        }
    }

    public String name() {
        return name;
    }

    private String name;
}
```

And on the other side of the pipeline ... add the following code for the `Pharmacy` class in the `Pharmacy.java` file:


```Java
public class Pharmacy {
    public Pharmacy(String pharmacyName) {
        mPharmacyName = pharmacyName;
    }
    public boolean send(Transporter t) {
        Medicine advil = new Medicine("Advil");
        System.out.println(String.format("Sending an %s.", advil.getMedicineName()));
        return t.goods.add(advil);
    }

    public String pharmacyName() {
        return mPharmacyName;
    }

    private String mPharmacyName;
}
```

Now that we have all the components of our simulation, let's build something that will set the plot in motion!

## As The World Turns

The `HospitalRunner` class will _run_ the simulation. In a simple simulation, we will play out what happens when a particular pharmacy sends some medicine to a receiving hospital via a transporter. Our `HospitalRunner` class will expose a single method named `run` that will script a simulation scenario. Add the following code for the `HospitalRunner` class to the `HospitalRunner.java` file:

```Java
public class HospitalRunner {
    public static void run() {
        Transporter semiTruck = new Transporter();

        Pharmacy cvs = new Pharmacy("CVS at 7500 Beechmont Avenue");

        cvs.send(semiTruck);

        Hospital uc = new Hospital("World Famous University of Cincinnati Children's Hospital");

        uc.receive(semiTruck);
    }
}
```

(Yes, that _is_ the address where I get *my* medicines.) I think that the script for the simulation is fairly reasonable:

1. Create a semitruck that will do the `Transporter`ing.
2. Create a pharmacy that will do the sending.
3. Direct the pharmacy to send its medicine.
4. Act as a freight broker that actually does the shipping.
5. Create a hospital that will do the receiving.
6. Direct the hospital to receive the shipment.

There's still one part of the simulation to wire up. Remember how we described the way that the JVM starts an application? Well, that `main` function is actually in the `SupplyChain` class and not in the `HospitalRunner` class. So, we will need to modify the `main` function in the `SupplyChain` class to actually do the `HospitalRunner` simulation. Add the following single line to the `main` function in the `SupplyChain` class:

```Java
HospitalRunner.run();
```

Wait, don't we have to instantiate a `HospitalRunner` first before we can use its `run` method? Ah, no! Look closely at the code that we used in the `HospitalRunner` and you will see how the method is `static`! Did you catch that? Tricky, tricky.

## Checkpoint 1

Check the output of your code at this point. Does it run? What do you see printed when it executes?

If your output does not look like

```
Sending an Advil.
Receiving a Advil.
```

double back and check to see if you can find the mistake.

## What Can Brown Do For You?

These days, operations that claim to offer the fastest, most reliable transportation are all around. They are the subcontractors to whom Amazon, FedEx and others farm out the painstaking process of actually moving goods from one place to another (and paying their employees fair wages). A pharmacy ideally only wants to ship their goods using someone they can trust "When it absolutely, positively has to be there overnight". One of the conditions a pharmacy uses to determine the trustworthiness of a shipper is the transporter's ability to maintain a consistent temperature. The pharmacy will only ship a medicine with a transporter whose temperature-control parameters are compatible with the safe temperatures of the medicine they are sending.

To equip our simulation to support verifying such a requirement, we will have to add some features to the `Transporter` class. Let's add two fields, a low temperature and a high temperature, to keep track of the low and high temperature, respectively, that the transporter guarantees. Add the following code to the `Transporter` class:

```Java
    private double mLowTemperature, mHighTemperature;
```

Where before there was no constructor for the `Transporter` class, we will need one so that the client of the `Transporter` class is able to specify these parameters when a new instance is created. Add the following constructor to the `Transporter` class:


```Java
    public Transporter(String transporterName, double lowTemp, double highTemp) {
        mTransporterName = transporterName;
        mLowTemperature = lowTemp;
        mHighTemperature = highTemp;
    }
```

Not quite done yet.

## We've Been Exposed

Well, well, well. We're in quite a pickle now. Why? In the design of our `Transporter` class we exposed the `goods` field to the client. If any client of the `Transporter` can add goods to the, er, `goods` field (willy nilly) then there is no way for the `Transporter` to enforce a guarantee that it only accept packages that it can be sure to keep safe!

We've unwittingly found ourselves snakebit by failing to observe one of the prime directives of OOP: data hiding (a.k.a. encapsulation). To fix the flaw, we will have to go back and do some surgery (the puns keep on coming). Better to bite the bullet (another one!) now and make the fix than continue to pay the price as the software evolves.

First, let's correct the error and make the `goods` field private:

```Java
private List<Medicine> goods;
```

Now we will have to offer a mechanism for the client to be able to add a medicine to the `Transporter`. Obviously the method will accept an instance of the  `Medicine` class to ship. But what does the method return? Let's have it return `true` in the case where the instance of the `Medicine` that the client wants to ship is compatible with the `Transporter` and can be carried and `false` otherwise. Add the following implementation of the `load` method to the `Transporter` class:

```Java
    public boolean load(Medicine itemToLoad) {
        if (itemToLoad.isTemperatureRangeAcceptable(mLowTemperature, mHighTemperature)) {
            System.out.println(String.format("Adding a %s to the transporter.", itemToLoad.getMedicineName()));
            goods.add(itemToLoad);
            return true;
        }
        return false;
    }
```

For the other side, the receiver side, we will need to provide methods to tell whether the `Transporter` has cargo and methods to retrieve that cargo. Add the following implementation of the `isEmpty` and `unload` methods to the `Transporter` class:

```Java
    public Medicine unload() {
        return goods.remove(0);
    }
    public boolean isEmpty() {
        return goods.isEmpty();
    }
```

Finally, we finish paying back our technical debt by updating the `Pharmacy` and `Hospital` classes to use the new functionality of the `Transporter`. Update the `Pharmacy`'s `send` function with this code:

```Java
    public boolean send(Transporter t) {
        Medicine advil = new Medicine("Advil");
        if (t.load(advil)) {
            System.out.println(String.format("Sending %s on the %s transporter.", advil.getMedicineName(), t.getTransporterName()));
            return true;
        }
        System.out.println(
                String.format("Cannot load %s on to the %s transporter.", advil.getMedicineName(), t.getTransporterName()));
        return false;
    }
```

and the `Hospital`'s `receive` method with this code:

```Java
    void receive(Transporter t) {
        while (!t.isEmpty()) {
            Medicine unloaded = t.unload();
            System.out.println(String.format("Receiving %s off the %s transporter.", unloaded.getMedicineName(), t.getTransporterName()));
        }
    }
```

Finally, our simulation will need to be upgraded to take advantage of the added fidelity. Change the instantiation of the `Transporter` class to


```Java
        Transporter semiTruck = new Transporter("LaserShip", 30.0, 80.0);
```

in the simulation scenario script.

## Checkpoint 2

Do the results of a run of the simulation change at all as a result of the added conditions imposed? If the output from executing your program does not match the following, please go back and check for mistakes!

```
Adding a Advil to the transporter.
Sending Advil on the LaserShip transporter.
Receiving Advil off the LaserShip transporter.
```

## Box Office Blockbuster

Any fly-by-night shipper can move Ibuprofen from one place to another. It's an entirely different matter to provide delivery services between pharmacy and hospital of an advanced drug like Activase.

To simulate shipping different medicines, we have a few software-design options from which to choose: We could simply change the implementation of the `Medicine` class' `minimumTemperature` and `maximumTemperature` to return different constants. That doesn't seem like a good solution -- it does not really allow us the chance to have many different types of medicines, each of which with a particular safe-temperature range. Are there other options?

Of course there are! Inheritance to the rescue -- in particular, _implementation inheritance_. Let's create a subtype of the `Medicine` class that customizes the functionality of those two temperature-related virtual functions in a way that more closely models the requirements for shipping a thrombolytic drug. We will name the class `Thrombolytic`. In `Java`, all `public` classes have to exist in a file with the same name as the class (with a `.java` suffix). Add the following class declaration to the `Thrombolytic.java` file:

```Java
public class Thrombolytic extends Medicine {

}
```

Immediately you notice that the Java compiler is *not* happy. Why does it yell at me? The class declaration that we created tells Java that the `Thrombolytic` class (we should have picked an easier name to type) is a subtype of the `Medicine` class (using the `extends` keyword) and Java knows that the `Medicine` class has a single constructor that takes a `String` parameter (remember that we construct a `Medicine` by giving it a name?). Java is telling us that, in order to construct something that is a subtype of the `Medicine` class, the `Thrombolytic` class must invoke its supertype's constructor. Fine, we'll oblige. Add the following constructor code to the `Thrombolytic` class:

```Java

public Thrombolytic() {
  super("Thrombolytic");
}
```

Here we define a constructor for the `Thrombolytic` class that takes no parameters and uses the `super` keyword to invoke functionality in the supertype -- in particular, we are invoking one of the supertype's constructors. That constructor, remember, takes a `medicineName` as a parameter and we use the pre-determined "Thrombolytic" as the value. Problem solved!

With that out of the way, we can get down to accomplishing our actual goal: customizing the safe temperatures of this medicine. A thrombolytic medicine generally [needs to be stored between 39.2 degrees and 41 degrees (Fahrenheit) to remain safe](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3621317/). Given that, in our implementation of the `Thrombolytic` class we will _override_ the `Medicine` class' implementation of the `minimumTemperature` and `maximumTemperature` function to provide a different behavior when those methods are invoked on an instance of a `Thrombolytic` class as opposed to an instance of a `Medicine` class. Here is the first opportunity to see the power of _virtual methods_: A virtual method is one that is implemented by a class at several points throughout an inheritance hierarchy. In an OOP language, at runtime, the version of the virtual method specific to an object's _actual_ type is chosen for execution (this feature is called _open recursion_ or _late binding_). What do we mean by "actual"? In an OOP language (like Java), it is possible to assign an instance of class `C` to a variable, call it `v`, whose type restricts it to hold instances of class `B`, where `B` is a superclass of `C`. Despite the fact that the type of `v` would indicate that it holds an instance of a `B`, it _actually_ holds an instance of `C`. The user of `v` is none-the-wiser because of _behavioral subtyping_ (aka, the Liskov Substitution Principle). However, we want the instance of the virtual method associated with the class `C` to be invoked in this case even though the type of the variable indicates that is an instance of `B`. That's a mouthful. Let's see it work in practice.

Add the following method implementations to the `Thrombolytic` class:

```Java
  @Override
  public double minimumTemperature() {
    System.out.println("Getting the minimum safe temperature for a Thrombolytic drug.");
    return 39.2;
  }

  @Override
  public double maximumTemperature() {
    System.out.println("Getting the maximum safe temperature for a Thrombolytic drug.");
    return 41.0;
  }
```

Nothing there is new, except for the `@Override` syntax. What gives? This is a so-called _[annotation](https://docs.oracle.com/javase/specs/jls/se18/html/jls-9.html#jls-9.7)_. In Java, annotations are non-functional code that provides an indication to the compiler about the programmer's intent and helps the compiler catch common errors. The [`@Override` annotation](https://docs.oracle.com/javase/tutorial/java/annotations/predefined.html) indicates to the compiler that the programmer intends to override a virtual method from the superclass. The compiler then can check to make sure that the method does properly override a method from the baseclass and will generate an error/warning if it does not.

Why is this helpful?

Let's say that I was typing too fast and wrote the following instead of what I wrote above:

```Java
  public double maximumTemperatur() {
    System.out.println("Getting the maximum safe temperature for a Thrombolytic drug.");
    return 41.0;
  }
```

Notice the typo? Yeah, neither did I! It's hard to. The code above would compile but your code would not be providing an alternate, subclass-specific implementation of the `maximumTemperature` function. Had the programmer used the `@Override` annotation, the compiler would have flagged the error. Pretty useful stuff!

Now, let's update our simulation to attempt to ship one of these high-caliber medicines. First, update the code in the `Pharmacy` class to ship thrombolytic medicines. Update the `send` method of the `Pharmacy` class to look like:

```Java
    public boolean send(Transporter t) {
        Medicine advil = new Medicine("Advil");
        if (t.load(advil)) {
            System.out.println(String.format("Sending %s on the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Medicine activase = new Thrombolytic();
        if (t.load(activase)) {
            System.out.println(String.format("Sending %s on the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }
        return true;
    }
```

Notice, in particular, how we are assigning to a variable, `activase`, whose type is `Medicine`, an object whose type is `Thrombolytic`. Does that remind you of the situation we discussed above (using `C` anywhere that we expected a `B` and not knowing the difference between `C` as a _subtype_ of `B` and `B` itself?).

## Checkpoint 3

Run the simulation. What do you notice? If your output does not match what is given below, go back and look for any typos:

```
Adding a Advil to the transporter.
Sending Advil on the LaserShip transporter.
Getting the minimum safe temperature for a Thrombolytic drug.
Cannot load Thrombolytic on to the LaserShip transporter.
Receiving Advil off the LaserShip transporter.
```

When you run the simulation you will notice that, indeed, Java is doing the right thing and selecting the overriden `minimumTemperature` and `maximumTemperature`! Exactly what we want -- the `activase` cannot be loaded on to the LaserShip transporter because it does not meet the requirements for keeping the medicine safe.

## A Better Mousetrap

Let's update `HospitalRunner` to simulate what happens when a better transportation provider is used that supports a more conducive environment for chemimcally volatile medicines. Change the `run` method of the `HospitalRunner` class to look like:

```Java
    public static void run() {
        Transporter priorityDispatch = new Transporter("Priority Dispatch", 40.0, 41.0);

        Pharmacy cvs = new Pharmacy("CVS at 7500 Beechmont Avenue");

        cvs.send(priorityDispatch);

        Hospital uc = new Hospital("World Famous University of Cincinnati Children's Hospital");

        uc.receive(priorityDispatch);
    }
```

and rerun the simulation! Your output should look like this:

```
Adding a Advil to the transporter.
Sending Advil on the Priority Dispatch transporter.
Getting the minimum safe temperature for a Thrombolytic drug.
Getting the maximum safe temperature for a Thrombolytic drug.
Adding a Thrombolytic to the transporter.
Sending Thrombolytic on the Priority Dispatch transporter.
Receiving Advil off the Priority Dispatch transporter.
Receiving Thrombolytic off the Priority Dispatch transporter.
```

Now that we are using a more specialized transportation provider we can safely ship the cutting-edge medicine. Pretty cool!

## I'm Not Buying What You Are Selling

To this point our restrictions have focused on whether the shipper feels comfortable sending something and _not_ on whether the receiver feels safe [accepting a package](https://en.wikipedia.org/wiki/The_Package_(Seinfeld)). This state of affairs must change. Hospitals are sometimes not equipped to properly handle potentially addictive medications. So-called controlled substances are given a schedule number by the [Drug Enforcement Administration](https://www.dea.gov/drug-information/drug-scheduling) (from 1 to 5), with 1 being the most addictive (and least medically necessary) and 5 being the least addictive. We will add a "sixth" category that indicates that a medicine is not a controlled substance and, therefore, does not have a schedule. To hold this information about a medicine we will add a field to the `Medicine` class.

Just what type should that field be given? We _could_ use an integer -- after all, 1, 2, 3, 4, 5 and 6 are all numbers. However, so are 49, 32 and 67, but those aren't valid schedules. If we used an integer we would also be forced to use, gasp, magic values! In other words, we would have the constants `1`, `2`, `3`, etc littered throughout our code!

Think about all the different possible valid values for a variable that holds a schedule. We _listed_ them above. You could even say that we _enumerated_ them. That gives me an idea! Let's use an _enumerated type_: types that only hold certain, pre-determined valid values. Enumerated types allow us to give the valid values of the type nice, descriptive names. What is even cooler is that in Java an enumerated type is just a special type of a class. As a result, you can do things with enumerated types in Java that you cannot do in other languages (like write a constructor!). Boom.

Again, all public classes (and, therefore, enumerated types) must be put in a separate, appropriately named file. Define the `MedicineSchedule` enumeration this way in the `MedicineSchedule.java` class:

```Java
public enum MedicineSchedule {
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Uncontrolled(6);

    private int mSchedule;
    private MedicineSchedule(int schedule) {
        mSchedule = schedule;
    }

    public String asString() {
        switch (mSchedule) {
            case 1:
                return "Schedule One Medicine";
            case 2:
                return "Schedule Two Medicine";
            case 3:
                return "Schedule Three Medicine";
            case 4:
                return "Schedule Four Medicine";
            case 5:
                return "Schedule Five Medicine";
            default:
                return "Uncontrolled";
        }
    }
}

```

This enumerated type declaration shows the full power of enumerations in Java. The named enumerated values (`One`, `Two`, `Three`, etc) are, essentially, nicely named constructors. Using these values is the *only* valid way to create a value that is compatible with a variable whose type is the enumerated type `MedicineSchedule`. In other words,

```Java
MedicineSchedule schedule = MedicineSchedule.One;
```

is the only way to create a new variable with the `MedicineSchedule` type. To attempt to do

```Java
MedicineSchedule schedule = new MedicineSchedule(1);
```

, or anything else like that, is an error.

Great. Let's add a method to the `Medicine` class that will allow clients to ask about a `Medicine`'s schedule. In the superclass, `Medicine`, we will want that method to return some baseline or reasonable default schedule for all medicines in the absence of a more specific schedule provided by a subclass. The problem is that a default really doesn't exist! 

Imagine that a subclass of `Medicine` (in other words, a specific type of medicine, like a thrombolytic or a GLP-1 or any of the CAR-Ts) does not override the "get schedule" method from the superclass. What default, baseline value should the superclass return? Ideally it would return a value that indicates a baseline, lower bound. But, it seems dangerous to assume that if a subclass of the `Medicine` class simply omits an overriding implementation of this method that that the drug is uncontrolled. The superclass's implementation of the getter could assume that the drug is Schedule 1. That would keep the patient maximally safe. However, it would also prevent certain hospitals from stocking drugs that they could otherwise safely handle.

What are we to do?

Let's make this rule: If you want to be a `Medicine`, then you _must_ implement your own version of a getter method that returns its schedule. `Medicine` will not provide you with a default! Java gives us the power to say such a thing! How cool? Add the following _abstract method_ declaration to the `Medicine` class:

```Java
    public abstract MedicineSchedule getSchedule();
```

This causes a problem, doesn't it? If there is no implementation of a method that a declaration promises to exist, it is impossible to actually create an instance of that class. I mean, what would happen if you had created an instance of `Medicine` and you called `getSchedule` on that instance? There's no implementation! The presence of a single abstract method in a class in Java is enough to make the entire class abstract. An _abstract class_ itself cannot be instantiated. Only subclasses of an abstract class can be instantiated (remember the difference between `List` and `ArrayList`?). Change the declaration of the `Medicine` class so that it includes the `abstract` keyword:

```Java
public abstract class Medicine {
...
}
```

There are trickle-down effects of this change in the `Pharmacy` class. We can no longer instantiate a generic `Medicine` object and have it represent Advil. We'll fix that by creating an `Ibuprofen` class in the `Ibuprofen.java` file (in the same directory as the `Medicine.java` file) by adding the following code:

```Java
public class Ibuprofen extends Medicine {

    public Ibuprofen() {
        super("Ibuprofen");
    }

    @Override
    public MedicineSchedule getSchedule() {
        return MedicineSchedule.Uncontrolled;
    }

    @Override
    public double minimumTemperature() {
        return 30.0;
    }

    @Override
    public double maximumTemperature() {
        return 90.0;
    }
}
```

Now, back in the `Pharmacy` class we'll remove the instantiation of the `Medicine` class that represents Advil and use an `Ibuprofen` class instead:

```Java
        Medicine advil = new Ibuprofen();
```

One final bit of clean up -- we have to specify the schedule of the `Thrombolytic`. Add the following method declaration to the `Thrombolytic` class:

```Java
  @Override
  public MedicineSchedule getSchedule() {
    return MedicineSchedule.Uncontrolled;
  }
```

That's great, but so far we don't have any controlled substances available for shipping! So, let's create one. We'll call it `Oxycodone` (you should know by now where and how to create/name the file for such a class):

```Java
public class Oxycodone extends Medicine {

  public Oxycodone() {
    super("Oxycodone");
  }

  @Override
  public MedicineSchedule getSchedule() {
    return MedicineSchedule.Two;
  }
}
```

Based on the declaration of the `Oxycodone` class, what are going to be its minimum and maximum safe temperatures? Is the `@Override` annotation useful here? Let's respecify what the pharmacy in our simulation produces:

```Java
   public boolean send(Transporter t) {
        Medicine advil = new Ibuprofen();
        if (t.load(advil)) {
            System.out.println(String.format("Sending %s on the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Medicine activase = new Thrombolytic();
        if (t.load(activase)) {
            System.out.println(String.format("Sending %s on the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Medicine oxycontin = new Oxycodone();
        if (t.load(oxycontin)) {
            System.out.println(String.format("Sending %s on the %s transporter.", oxycontin.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", oxycontin.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        return true;
    }
```

Great! We are all done adding the plumbing in our simulation to support categorizing medicines based upon their schedule. Now, let's add some functionality to the receiver (the `Hospital`) to reject medicines that are controlled substances! Update the `receive` method in the `Hospital` class like this:

```Java
    void receive(Transporter t) {
        while (!t.isEmpty()) {
            Medicine unloaded = t.unload();
            System.out.println(String.format("Checking whether Hospital can receive %s.", unloaded.getMedicineName()));
            if (unloaded.getSchedule() != MedicineSchedule.Uncontrolled) {
                System.out.println(String.format("Hospital cannot receive controlled substances and %s is a %s.",
                        unloaded.getMedicineName(), unloaded.getSchedule().asString()));
            } else {
                System.out.println(String.format("Accepted a shipment of %s.", unloaded.getMedicineName()));
            }
        }
    }
```

Notice how we are able to use the named values of the `MedicineSchedule` enumerated class when comparing the return value of the `getSchedule` method? That makes our code look really, really nice. And then, what's even better is that we can invoke the `asString` method on a value whose type is `MedicineSchedule` and it will generate a nicely formatted string for us to print!

## Checkpoint 4

If your output does not match the output below, go back and check for any typos:

```
Adding a Ibuprofen to the transporter.
Sending Ibuprofen on the Priority Dispatch transporter.
Getting the minimum safe temperature for a Thrombolytic drug.
Getting the maximum safe temperature for a Thrombolytic drug.
Adding a Thrombolytic to the transporter.
Sending Thrombolytic on the Priority Dispatch transporter.
Adding a Oxycodone to the transporter.
Sending Oxycodone on the Priority Dispatch transporter.
Checking whether Hospital can receive Ibuprofen.
Accepted a shipment of Ibuprofen.
Checking whether Hospital can receive Thrombolytic.
Accepted a shipment of Thrombolytic.
Checking whether Hospital can receive Oxycodone.
Hospital cannot receive controlled substances and Oxycodone is a Schedule Two Medicine.
```

## Part 2: Where Do We Stand?

Let's take stock of where we are now. We have several classes from which we can instantiate objects. Those objects can send messages to one another to solve a problem -- the definition of an object-oriented design/solution. Some of the classes form a hierarchy and others do not. We have classes with static methods, we have classes that are abstract. We have enumerated types. We have virtual methods that override default implementations. We've used lots of Java's power.

But, like good computer scientists, we always want more! Why can't our `Transporter` class support transporting something other than `Medicine`s? After all, a semi truck or a Sprinter van can hold pretty much anything in their cargo space. Why is our simulation limited to creating transporters that can only move medicines? A great question!

What field in the `Transporter` class is responsible for simulating the cargo space? The `goods` field! We declared it as a `List` and then assigned it an instance of a subclass (which we assessed to also be, techically, a subtype according to the Java specification) of the `abstract` `List` class, the `ArrayList`. Remember that `List` and it's subclass `ArrayList` are generic classes. They require a type parameter when they are being declared and instantiated. The type parameter defines the type of the entity that the `List` (in our case, an `ArrayList`) can hold. At this point, our `Transporter`'s `goods` is declared to hold instances of the `Medicine` class (or one of its subclasses).

To make our `Transporter` more flexible, we are going to need to define a class from which anything that we can put in a `Transporter` will descend! This feat is easier said than done!


If we want the `Transporter` to support transporting both `Medicine`s and, say, `Paint`s, perhaps we could conjure up the notion that both `Medicine` and `Paint` are forms of chemical compounds. In other words, we could define a class `ChemicalCompound` and say that `Medicine` and `Paint` derive from `ChemicalCompound` so that the `Transporter` could carry both when its cargo (`goods`) is declared to hold only instances of that type (or subclasses, of course).

That seems like a stretch, doesn't it?

What if we want to define a `Transporter` that supports safely transporting shelter pets and fine art? We are going to have a hard time coming up with a class from which they can both descend -- without it seeming totally absurd!

The good news is that Java helps us out. Every class defined in Java implicitly inherits from the `Object` class! The `Object` class is at the root of the inheritance hierarchy and all classes descend therefrom.

## Upgrading `Transporter`

Let's take advantage of this fact and update the `Transporter` to support transporting _any_ type of entity. First, let's change the declaration of the `goods` field:

```Java
    private List<Object> goods;
```

And now we have to change code in our instance initializer:
```Java
        goods = new ArrayList<Object>();
```

But the changes don't end there!

We exposed a `load` method that adds an item to the `Transporter`'s cargo bay. Because we want the transporter to be as flexible as possible, we have to redeclare that method to accept any entity that is an instance of the `Object` class. In other words, we can no longer make any assumptions about the types of variables that we can add to our cargo bay:

```Java
    public boolean load(Object itemToLoad) {
        if (itemToLoad.isTemperatureRangeAcceptable(mLowTemperature, mHighTemperature)) {
            System.out.println(String.format("Adding a %s to the transporter.", itemToLoad.getMedicineName()));
            goods.add(itemToLoad);
            return true;
        }
        return false;
    }
```

We also exposed an `unload` method that unloads (and `return`s) an item from the `Transporter`'s cargo bay. We wrote the return type for the method as `Medicine` because we knew that the `Transporter`'s cargo bay (the `goods`) only held instances of the `Medicine` class (or its subclasses). Now, though, we can't be so confident. The entities in the `goods` can be anything -- we only know that they are instances of the `Object` class. So, we have to change the method declaration:

```Java
    public Object unload() {
        return goods.remove(0);
    }
```

What does that do to the existing clients of the `Transporter`? In particular, what changes need to be made to the `Pharmacy` and the `Hospital`? There are no changes that need to be made to the `Pharmacy`. Any instance of the `Medicine` class, what we had written the `Pharmacy` class to add to the `Transporter`, is _also_ an instance of `Object` so we can add those to the `Transporter` without any problem.

What happens on the other side? On the `Hospital` side? Well, in the `receive` method of the `Hospital` class, the schedule of every medicine that is unloaded from the `Transporter` is checked. And now we see our first problem with the change from a `Transporter` that holds instance of the `Medicine` class (or subclasses) to a `Transporter` that holds instances of the `Object` class (or subclasses): The type of the variable that we assign the result of the `unload` method must be `Object` and cannot be `Medicine`:

```Java
    void receive(Transporter t) {
        while (!t.isEmpty()) {
            Object unloaded = t.unload();
            System.out.println(String.format("Checking whether Hospital can receive %s.", unloaded.getMedicineName()));
            if (unloaded.getSchedule() != MedicineSchedule.Uncontrolled) {
                System.out.println(String.format("I cannot receive controlled substances and %s is a %s.",
                        unloaded.getMedicineName(), unloaded.getSchedule().asString()));
            } else {
                System.out.println(String.format("Accepted a shipment of %s.", unloaded.getMedicineName()));
            }
        }
    }
```

And now you immediately see Java's unhappiness: the compiler cannot guarantee that an instance of the `Object` class has the `getSchedule` and the `getMedicineName` method! What are we to do? There's two options: an easy (but potentially incorrect) solution and a hard (but defensive, and always correct) solution. Let's start with the easy solution first.

## When You Assume

The easy solution is to assume that the objects that are unloaded from the `Transporter` that the hospital is given are actually instances of the `Medicine` class (or subtypes). We can tell Java that we, the programmer, know better than it does by using a _cast_. Though the compiler can only guarantee that instance that comes back from the `Transporter` has the methods that are defined for instances of the `Object` class, as the implementer of the `Hospital` class, we can assume that no one in their right mind would ship a hospital something other than `Medicine`s.

```Java
    void receive(Transporter t) {
        while (!t.isEmpty()) {
            Medicine unloaded = (Medicine)t.unload();
            System.out.println(String.format("Checking whether Hospital can receive %s.", unloaded.getMedicineName()));
            if (unloaded.getSchedule() != MedicineSchedule.Uncontrolled) {
                System.out.println(String.format("Hospital cannot receive controlled substances and %s is a %s.",
                        unloaded.getMedicineName(), unloaded.getSchedule().asString()));
            } else {
                System.out.println(String.format("Accepted a shipment of %s.", unloaded.getMedicineName()));
            }
        }
    }
```

Although we cannot execute this code (there are still compiler errors to fix -- see below), the code that we have written to this point will perform the same as the code we wrote that was `Medicine`-specific. Great!

But, leaving this code in our product is like walking a very thin rope at great heights in gale-force winds -- we are definitely living dangerously. It's like living almost as dangerously as, dramatic pause, bowling without bumpers!

Let's write a class that will simulate something that a `Pharmacy` might reasonably ship to a `Hospital` but is not a `Medicine` -- a medical device. We will assume that the device is a Jarvik Artifical Heart -- `Jarvik`. Create the `Jarvik` class in the `Jarvik.java` file with the following code:


```Java
public class Jarvik {
  Jarvik(String deviceSerialNumber) {
    serialNumber = deviceSerialNumber;
  }

  public String getMedicineName() {
    return "Jarvik Artificial Heart";
  }

  public MedicineSchedule getSchedule() {
    return MedicineSchedule.Uncontrolled;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public boolean isTemperatureRangeAcceptable(Double lowTemperature, Double highTemperature) {
    if (30.0 <= lowTemperature && highTemperature <= 90.0) {
      return true;
    }
    return false;
  }

  private String serialNumber;
}
```

Now we'll upgrade the simulation so that the `Pharmacy` attempts to ship one of these miracle devices to the `Hospital`:

```Java
    public boolean send(Transporter t) {
        Medicine advil = new Ibuprofen();
        if (t.load(advil)) {
            System.out.println(String.format("Sending %s on the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", advil.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Medicine activase = new Thrombolytic();
        if (t.load(activase)) {
            System.out.println(String.format("Sending %s on the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", activase.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Medicine oxycontin = new Oxycodone();
        if (t.load(oxycontin)) {
            System.out.println(String.format("Sending %s on the %s transporter.", oxycontin.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", oxycontin.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        Jarvik heart = new Jarvik("01j9a9lk71");
        if (t.load(heart)) {
            System.out.println(String.format("Sending %s on the %s transporter.", heart.getMedicineName(),
                    t.getTransporterName()));
        } else {
            System.out.println(String.format("Cannot load %s on to the %s transporter.", heart.getMedicineName(),
                    t.getTransporterName()));
            return false;
        }

        return true;
    }
```

If we ran the simulation that contained this version of the `load` method in the `Pharmacy` class (which we can't -- again, see below), we would get a very unhappy JVM: It would report an error when it attempted to perform the cast between the unloaded item and `Medicine`!

```
Exception in thread "main" java.lang.ClassCastException: class edu.uc.cs3003.medava.Jarvik cannot be cast to class edu.uc.cs3003.medava.Medicine (edu.uc.cs3003.medava.Jarvik and edu.uc.cs3003.medava.Medicine are in unnamed module of loader 'app')
        at edu.uc.cs3003.medava.Hospital.receive(Hospital.java:10)
        at edu.uc.cs3003.medava.HospitalRunner.run(HospitalRunner.java:13)
        at edu.uc.cs3003.medava.SupplyChain.main(SupplyChain.java:6)
```

Why? Because the `Hospital` eventually unloads an instance of the `Jarvik` class and that is __not_ an instance of `Medicine` (or one of its subtypes) so the cast is invalid! But, but, but -- I know, I see it, too!

`Jarvik` _is_ a class that implements all the methods that we need to be able to add it to the `Transporter` -- there's a `getMedicineName`, there's a `isTemperatureRangeAcceptable`, there's even a `getSchedule`, even though it doesn't necessarily make sense. Java is not impressed. That static cast cannot complete because the static cast relies on _nominal type equivalency_ and a `Jarvik` is not a `Medicine` (or subtype thereof). If Java used structural type equivalency, then we might have had a chance! But here ... no dice!

## Mirror, Mirror On The Wall!

We've avoided the elephant in the room for long enough -- none of the changes that we made above even yielded code that compiled let alone executed correctly! The compiler first encounters a problem in the `load` method of the `Transporter` class! Why? Because the `load` method relies on the fact that it can invoke a method named `isTemperatureRangeAcceptable` on the item to load to determine whether to follow the client's command.

But, again, the type of the parameter is `Object` (because we wanted our `Transporter` to have maximum flexibility)! We know that casting is probably not a good solution. I mean, how could we determine how to write the cast when we've left ourselves wide open to accepting an instance of absolutely any Java type?

There's still hope, though! Java provides an amazing set of functionality under the umbrella of _reflection_. Reflection is a feature of a programming language that "allows its programs to have run-time access to their types and structures and to be able to dynamically modify their behavior." (Sebesta)

Our simulation software (in particular, the `Transporter` and `Hospital` class) could (!!) use reflection to dynamically check whether the item to be loaded and shipped has the proper validation methods (i.e., `isTemperatureRangeAcceptable`, `getSchedule`, `getMedicineName`). Based on the result of that check, the `Transporter` and `Hospital` classes will modify their behavior:

1. In the `Transporter` class, if the `isTemperatureRangeAcceptable` function is not available on the method being `load`ed, the function will assume that it cannot be added to the transporter's cargo bay; if the validation method _is_ available, then the `load` method of the `Transporter` class will call it just as it did before to make the "can ship" determination!
1. In the `Hospital` class, if the `getSchedule` method is not available during `receive`, the method will assume that it cannot be received; if the validation method _is_ available, then the `receive` method of the `Hospital` class will call it just as it did before!

Totally, totally cool! The syntax of reflection is a little mind bending and a little meta. Here's the updated code for the `load` method of the `Transporter` class:

```Java
    public boolean load(Object itemToLoad) {
        try {
            Method isTemperatureRangeAcceptableMethod = itemToLoad.getClass().getMethod("isTemperatureRangeAcceptable",
                    Double.class, Double.class);
            boolean resultOfMethodCall = (boolean) isTemperatureRangeAcceptableMethod.invoke(itemToLoad,
                    Double.valueOf(mLowTemperature), Double.valueOf(mHighTemperature));
            if (resultOfMethodCall) {
                goods.add(itemToLoad);
            }
            return resultOfMethodCall;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            return false;
        }
    }
```

You will also need to `import` the appropriate, supporting packages:

```Java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
```

There is definitely a ton going on here. First, we see a new type of control-flow construct known as the `try`-`catch` statement. The semantics go like this: The statements in the so-called `try` block are executed from top to bottom. If any of those statements (or statements that are in methods called by those statements, and so on!) _throws_ an exception, we know that an unexpected situation has transpired! The exception that is _thrown_ is an object just like anything else in Java and, therefore, has a type and a value. The `catch` phrase (get it?) lists a sequence of types and then gives a variable name. Java looks sequentially throw the `catch` phrases to find a class that matches the _actual_ type of the thrown exception. The code in the first matching phrase is executed with the exception assigned to the named variable (`e` in this case).

Here is a stripped down example of code that uses `try`-`catch` to show just how flexible Java is with respect to handling exceptions:

```Java
      try {
            ...
            throw new BadHairDayException();
            ...
        } catch (BadNameException bne) {
            System.out.println("There was a bad name exception.");
        } catch (BadHairDayException bhde) {
            System.out.println("There was a bad hair day exception.");
        } catch (Exception bae) {
            System.out.println("There was a generic exception.");
        }
```

will print

```console
There was a bad hair day exception.
```

On the other hand,

```Java
      try {
            ...
            throw new BadNameException();
            ...
        } catch (BadNameException bne) {
            System.out.println("There was a bad name exception.");
        } catch (BadHairDayException bhde) {
            System.out.println("There was a bad hair day exception.");
        } catch (Exception bae) {
            System.out.println("There was a generic exception.");
        }
```

will print

```console
There was a bad name exception.
```

Because every exception is ultimately a subclass of `Exception`, programmers sometimes add an exception handler that will match any thrown exception:

```Java
      try {
            ...
            throw new BadAttitudeException();
            ...
        } catch (BadNameException bne) {
            System.out.println("There was a bad name exception.");
        } catch (BadHairDayException bhde) {
            System.out.println("There was a bad hair day exception.");
        } catch (Exception bae) {
            System.out.println("There was a generic exception.");
        }
```

will print

```console
There was a generic exception.
```

Now, let's get on with the real work. Our first task is to determine if the object the user wants to load has a method of a certain name and, if so, whether that method takes the types of parameters that we expect it to. The `getMethod` method takes the name of a method as the first parameter and then a variably sized list of _classes_ that describe the parameters of the method named in the first parameter:

```Java
            Method isTemperatureRangeAcceptableMethod = itemToLoad.getClass().getMethod("isTemperatureRangeAcceptable",
                    Double.class, Double.class);
```

`getMethod` searches the associated instance (`itemToLoad` in this case) to find a handle for a method with the give name (`isTemperatureRangeAcceptable` in this case) that takes the specified number and type of arguments (two `Double` instances in this case). If there is no such method (or some other error occurs), then an exception is thrown. As soon as the first exception is thrown in a `try` block, no further code in the `try` block is executed! The code in the `catch` block that we implemented simply returns `false` to indicate (defensively) that the item to be loaded cannot be shipped in the current transporter. The method handle that is (conditionally) found is an instance of a class -- just like _everything_ in Java (with a few exceptions). That class offers its clients an `invoke` method that gives them the power to invoke that found method on a particular object with a particular set of parameters. If you squint,


```Java
            boolean resultOfMethodCall = (boolean) isTemperatureRangeAcceptableMethod.invoke(itemToLoad,
                    Double.valueOf(lowTemperature), Double.valueOf(highTemperature));
```

looks very much like

```Java

        boolean resultOfMethodCall = itemToLoad.isTemperatureRangeAcceptable(lowTemperature, highTemperature);
```

and that's no coincidence -- they are doing the exact same thing!

Now, if you don't think _that's_ cool, then ...

Remember all that time ago when we talked about _why_ we had to define the `isTemperatureRangeAcceptable` to take two `Double`s (which are actually classes and not primitives) instead of `double`s (which are primitives and not classes)? Well, now you know the answer. If we declared the `isTemperatureRangeAcceptable` method in a way that it took two primitive `double`s we would not have been able to use reflections's `getMethod` function[^3]. Do you see the problem? The `getMethod` method expects its caller to describe the method to be looked up to have parameters that can be described with a list of classes. Well, primitives do not have classes. So, we defined the function to take `Double`s -- a class that basically mimics the equivalent primitive type. Yes, it _is_ annoying!

[^3]: As of Java 5.0 (originally known as Java 1.5 internally) and the [third specification of the Java language](https://docs.oracle.com/javase/specs/jls/se6/jls3.pdf) there _is_ a Java feature named [autoboxing](https://docs.oracle.com/javase/tutorial/java/data/autoboxing.html) that does make it possible to use `getMethod` on methods that have primitive-typed parameters. To specify that the parameters are primitives rather than instances of `class`es, the caller of the `getMethod` method would use the [`.class`](https://docs.oracle.com/javase/specs/jls/se22/html/jls-15.html#jls-ClassLiteral) method on the primitive type. For example, if the `isTemperatureRangeAcceptable` method took two `double`s, the caller of the `getMethod` method would specify `double.class` as the parameter type. Neat!

Now let's turn our attention to the `Hospital` class and reflect (hi-ooooooooooooo) on the code there. The troublesome spot is the `receive` function where the `Hospital` assumes that the object unloaded from the `Transporter` has a certain set of methods. We will use reflection and the `getMethod` function to determine whether each object that is unloaded supports the required methods. Upgrade the `receive` method with the following code:

```Java

    void receive(Transporter t) {
        while (!t.isEmpty()) {
            try {
                Object unloaded = t.unload();
                Method getScheduleMethod = unloaded.getClass().getMethod("getSchedule");
                MedicineSchedule getScheduleMethodResult = (MedicineSchedule) getScheduleMethod.invoke(unloaded);
                Method getMedicineNameMethod = unloaded.getClass().getMethod("getMedicineName");
                String getMedicineNameMethodResult = (String) getMedicineNameMethod.invoke(unloaded);
                System.out.println(String.format("Checking whether Hospital can receive %s.", getMedicineNameMethodResult));
                if (getScheduleMethodResult != MedicineSchedule.Uncontrolled) {
                    System.out.println(String.format("Hospital cannot receive controlled substances and %s is a %s.",
                            getMedicineNameMethodResult, getScheduleMethodResult.asString()));
                } else {
                    System.out.println(String.format("Accepted a shipment of %s.", getMedicineNameMethodResult));
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // No need to do anything
            }
        }
    }
```

Don't forget to add the import statements:


```Java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
```

## Checkpoint 5:

Make sure that your program outputs the following. If it does not, go back and check for errors!

```
Sending Ibuprofen on the Priority Dispatch transporter.
Getting the minimum safe temperature for a Thrombolytic drug.
Getting the maximum safe temperature for a Thrombolytic drug.
Sending Thrombolytic on the Priority Dispatch transporter.
Sending Oxycodone on the Priority Dispatch transporter.
Sending Jarvik Artificial Heart on the Priority Dispatch transporter.
Checking whether Hospital can receive Ibuprofen.
Accepted a shipment of Ibuprofen.
Checking whether Hospital can receive Thrombolytic.
Accepted a shipment of Thrombolytic.
Checking whether Hospital can receive Oxycodone.
Hospital cannot receive controlled substances and Oxycodone is a Schedule Two Medicine.
Checking whether Hospital can receive Jarvik Artificial Heart.
Accepted a shipment of Jarvik Artificial Heart.
```

## Who's the Fairest of Them All

Reflection is a really powerful technique and a tool that we appreciate having in the toolchest. However, we should not rely on it when there are alternate methods. Reflection is definitely not the fastest way to accomplish our goal here. Determining at runtime whether an object supports certain methods is much slower than letting the compiler do it before the program is ever even executed.

Does Java give us a way to solve the problem? Let's first remind ourselves what the problem actually is:

1. We have a generic data structure (in this case the `Transporter` and `Hospital` classes) that works with instances of a wide range of classes where the only requirement on those objects' types is that they support a (very limited) number of methods (e.g., the `isTemperatureRangeAcceptable`, `getSchedule`, and `getMedicineName` methods).
2. The range of instances that the generic data structure must handle are of types that do not share a common base class.

The solution is one that is becoming increasingly common in OOP language (and even others that do not fit the traditional model of object orientation [e.g., go, and Rust]): interfaces (go calls them [interfaces](https://go.dev/ref/spec#Interface_types) and Rust calls them [traits](https://doc.rust-lang.org/book/ch10-02-traits.html)). The interface is a quasi-replacement for a type. You use an interface in place of the name of a type when you are declaring a field, variable or parameter. 

As we know, Java is a strongly and statically typed language. If we replace a type with an interface, how does that change the way that Java type checks our code? Java determines whether two types are compatible using nominal equivalency (the name of the names must match, or be subclasses of one another). If a parameter is declared with an interface, then Java determines whether another type is compatible according to whether or not the candidate type contains all the _methods_ declared in the interface! It's a very, very specific type of _structural type equivalency_ (without all the surprises!).

In Java, a programmer declares an interface using a syntax highly reminiscent of a class. The difference is that

1. There are no constructors
2. There are no fields
3. The method declarations don't have implementations

Here is the interface that we are going to define for our simulation:

```Java
public interface Shippable {
  public MedicineSchedule getSchedule();
  public String getMedicineName();
  public boolean isTemperatureRangeAcceptable(Double lowTemperature, Double highTemperature);
}
```

Add that to the `Shippable.java` file.

Once the interface is defined, we can use it in place of a type throughout Java. Use of an interface in place of a type in the code lets the compiler guarantee the object implements *at least* the methods declared in the interface. The object might support additional methods (the compiler won't know what they are!), but it guarantees that it is safe for us programmers to invoke at least those methods on that variable!

We previously used classes and inheritance to solve this very problem. However, remember above why we ended up deciding to use an `Object` as the type of the parameter to the `load` and `receive` methods: Because there was nothing else that a `Medicine` and, say, a `Jarvik` artificial heart have in common. The only thing that the objects we want to ship have in common are a certain set of methods. Interfaces give us the power to express just that requirement to the compiler, and nothing more! Pretty amazing!

Let's change the `Transporter` class to use the `Shippable` interface. First, modify the `goods` declaration:

```Java
private List<Shippable> goods;
```

and its instantiation in the instance initializer:

```Java
        goods = new ArrayList<Shippable>();
```

Then we'll change the methods `load` and `unload`. When you make the following changes, ***do not delete the version of the code that uses reflection -- just comment it out***.

```Java
    public Shippable unload() {
        return goods.remove(0);
    }

    public boolean load(Shippable itemToLoad) {
        if (itemToLoad.isTemperatureRangeAcceptable(mLowTemperature, mHighTemperature)) {
            return goods.add(itemToLoad);
        }
        return false;
    }
```

Now, remind me where that other use of reflection was that we want to remove? Oh, right! The `Hospital`. We'll change the implementation of the `receive` method. Again, when you make the following changes, ***do not delete the version of the code that uses reflection -- just comment it out***.

```Java
    void receive(Transporter t) {
        while (!t.isEmpty()) {
            Shippable unloaded = t.unload();
            System.out.println(String.format("Checking whether Hospital can receive %s.", unloaded.getMedicineName()));
            if (unloaded.getSchedule() != MedicineSchedule.Uncontrolled) {
                System.out.println(String.format("Hospital cannot receive controlled substances and %s is a %s.",
                        unloaded.getMedicineName(), unloaded.getSchedule().asString()));
            } else {
                System.out.println(String.format("Accepted a shipment of %s.", unloaded.getMedicineName()));
            }
        }
    }
```

The final step is to confirm with Java that our `Medicine` class (and its subclasses) and `Jarvik` class meet the specifications of the `Shippable` interface. To do that we use the `implements` keyword (you may resume making overwriting changes now!):

```Java
public class Jarvik implements Shippable {
    ...
}
```

```Java
public abstract class Medicine implements Shippable {
    ...
}
```

All done! That looks so much better than the treacherous solution using reflection or the code that lives dangerously by doing wildly optimistic casts between types!

## Checkpoint Omega

Make sure that your program in its final state has the following output:

```
Sending Ibuprofen on the Priority Dispatch transporter.
Getting the minimum safe temperature for a Thrombolytic drug.
Getting the maximum safe temperature for a Thrombolytic drug.
Sending Thrombolytic on the Priority Dispatch transporter.
Sending Oxycodone on the Priority Dispatch transporter.
Sending Jarvik Artificial Heart on the Priority Dispatch transporter.
Checking whether Hospital can receive Ibuprofen.
Accepted a shipment of Ibuprofen.
Checking whether Hospital can receive Thrombolytic.
Accepted a shipment of Thrombolytic.
Checking whether Hospital can receive Oxycodone.
Hospital cannot receive controlled substances and Oxycodone is a Schedule Two Medicine.
Checking whether Hospital can receive Jarvik Artificial Heart.
Accepted a shipment of Jarvik Artificial Heart.
```

## Conclusion

Some would say that we are all just living in a giant computer simulation. I don't believe it. However, we did work our way through creation of a very powerful simulation system that takes advantage of some very interesting parts of the Java OOP language. We learned about Java's

1. support for inheritance
2. static methods
3. abstract classes and methods
4. support for virtual methods using the `@Override` annotation
5. support for enumerated types with the `enum` class
6. tools for reflection
7. interfaces.

This quick walk through only scratches on the surface of Java's power. It is a very interesting language and I encourage you to continue to learn more about it as you grow as professional programmers.

## One More Thing ...

Along with the code that you've written, for this assignment please create and submit a file named `questions.txt` (a plain text file) that contains several (the number is up to you) questions you had about Java as we worked through the material above. The list of questions is worth 1/2 of your grade for this assignment. Please make them thoughtful. I will grade them based on how well they reflect your engagement with the material above and the Java language overall. Feel free to include questions you have about OOP, in general, too. To be clear: I do not expect you to _know_ the answer to the questions! In fact, quite the opposite! The more befuddled you are about something, the more likely that we will learn something together (because I can assure you that your questions will make me think!).

## Submitting

You will submit your response to this assignment to Gradescope as we have done in previous assignments. The autograder will expect that you submit *all* the `.java` files, the `pom.xml` file and a `questions.txt` file (in the same directory structure as they are in the skeleton). The easiest way to accomplish this gargantuan task is to create a zip file. Make sure that `pom.xml` and `questions.txt` are in the root of the directory structure of the zip file that you create. ***Please do not*** include any `.class` files (in other words, do not include the `target/` directory) in your submission. Make sure that the autograder awards you 50 points after you submit your code. If you have any trouble navigating the autograder's idiosyncracies, please contact me!