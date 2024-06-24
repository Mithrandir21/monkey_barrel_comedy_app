## Module separation
Application logic separated into individual modules provides numerous benefits, such as clear separation of concern, built time improvements, clear dependency management and more.

### Major benefits to module separation:

:tada: **Clear Separation of Concerns**
Separating related logic into specific modules prevents package, naming and location choices having to be made for each piece of logic that needs creating.

:clipboard: **Clear Understanding of Dependencies**
As modules only know of their own code, it will be necessary to explicitly state any dependencies, which in turn will show what logic is dependent on what other logic.

:bulb: **Small and Compact code**
Small pieces of logic, with clear dependencies, makes code readable and understandable, reducing complexity and allowing for better testing and development.

:bell: **Core Functionality + Features**
Modules allow core functionality, used in general scenarios, to be separate from specific features, used only occasionally and in specific scenarios.

:rocket: **Massively Improved Built Times**
Because each module is built separately and also re-built independently of other modules, only changed modules need to be re-compiled and re-built, allowing all unchanged modules to be reused.

<hr />

:warning: Note that the below image is a massive over-simplification of the complexities that go into a modular code, but it does demonstrate very well the intended idea behind modularisation.
<img src="https://miro.medium.com/max/1400/1*Ujp5FfuGSqmrAMik0BzORA.png">

<hr />

### :warning: Modularisation - Concerns
With every step forward in the sphere of software development, there are some trade-offs.

**Data Storage**
Because each module is separate, it is necessary to carefully consider the data storage needs of each module and the application as a whole. Any module specific data, which means data not needed by the rest of the app, should be stored in such a way that it does not break module separation, becoming unnecessarily exposed to the rest of the app.

**Transitive Dependencies**
Dependencies that are not direct, but come through another dependency are call Transitive Dependencies. It is important to keep this in mind when working with modules so that a modules dependencies are not forced onto other modules without an explicit need being present.

**Dependency Injection**
Using Dependency Injection requires some setup to allow dependency inject to work correctly in a multi-module application. It is by no means impossible and already in use through the software development world. However, it does require a somewhat better understanding of dependency injection than in monolithic app development. Things such as Transitive Dependencies and any tool limitations must be kept in mind to not break the setup.

**External Dependencies**
Dependencies external to the app and its modules must be kept uniform, i.e. same version, to not create any unexpected behaviour. For example, the same exact version of Compose must be used through all the application.

**Common Functionality**
Functionality that is shared by multiple modules, such as data classes, styling/theming and logging, must be separated out of each module and placed into their own module or a common module allowing for use by other modules. However, module specific functionality must be kept internal to each module. This assessment is not always easy to make and must be done carefully and considering the future needs of the app and other modules.

<hr />