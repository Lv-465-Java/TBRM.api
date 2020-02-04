package com.softserve.rms.constants;

public class AclQueries {

    public static final String CLASS_IDENTITY_QUERY = "select currval(pg_get_serial_sequence('acl_class', 'id'))";
    public static final String SID_IDENTITY_QUERY = "select currval(pg_get_serial_sequence('acl_sid', 'id'))";
    public static final String OBJECT_IDENTITY_PRIMARY_KEY_QUERY = "select acl_object_identity.id from acl_object_identity, acl_class where acl_object_identity.object_id_class = acl_class.id and acl_class.class=? and acl_object_identity.object_id_identity = cast(? as varchar)";
    public static final String FIND_CHILDREN_QUERY = "select obj.object_id_identity as obj_id, class.class as class from acl_object_identity obj, acl_object_identity parent, acl_class class where obj.parent_object = parent.id and obj.object_id_class = class.id and parent.object_id_identity = cast(? as varchar) and parent.object_id_class = (select id FROM acl_class where acl_class.class = ?)";
    public static final String FIND_OBJECTS_WITH_ACCESS =
            "SELECT " +
                    "    obj.object_id_identity AS obj_id, " +
                    "    class.class AS class " +
                    "FROM " +
                    "    acl_object_identity obj, " +
                    "    acl_class class, " +
                    "    acl_entry entry " +
                    "WHERE " +
                    "    obj.object_id_class = class.id " +
                    "    and obj.object_id_class = (SELECT id FROM acl_class WHERE acl_class.class = ?) " +
                    "    and entry.granting = true " +
                    "    and entry.acl_object_identity = obj.id " +
                    "    and entry.sid = (SELECT id FROM acl_sid WHERE sid = ?) " +
                    "GROUP BY " +
                    "    obj.object_id_identity, " +
                    "    class.class ";
    public AclQueries() {
    }
}
