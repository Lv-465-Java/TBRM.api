# TBRS
backend


Resource Template can be created inside the Resource Management System by a _manager_. While creating a Resource Template, manager can add parameters for a detailed specification of a specific Resource. Once Resource Template is executed, it becomes an example for a _registrator_ on how to create a certain Resource in the system. All parameter fields are required and have to be filled with proper values for a better description of a Resource.

There are several parameter types, which can be added to a Resource Template:

### Point
Field type that holds a single value. A _manager_ may restrict the type of input so that he is able to choose the point type **Integer**, **Double** or **String**.
### Range
The best option for describing a resource _“from-to”_ property. It may be **Integer** or **Double**.
### Area
Field type for entering _coordinates_ of the land. It can be only **Double** type.


```POINT_INT, POINT_DOUBLE, POINT_STRING;```
```RANGE_DOUBLE, RANGE_INT;```
```AREA_DOUBLE```

**Mention** that the Resource Relation entity is not a parameter type. It may be used only for reference between Resources.

**Manager** - user with a _manager_ role in Resource Management System.
**Registrator** - user with a _registrator_ role in Resource Management System.

### Person roles supported by Resource Management System: admin, manager, registrator, user.
