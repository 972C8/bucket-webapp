package ch.fhnw.bucket.data.domain.image;

import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * Fix PostgreSQL specific issue when storing byte[]
 *
 * https://stackoverflow.com/questions/3677380/proper-hibernate-annotation-for-byte
 *
 * https://stackoverflow.com/questions/35505424/how-to-read-bytea-image-data-from-postgresql-with-jpa
 */
public class PostgreSQLDialectCustom extends PostgreSQL82Dialect {

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor) {
        if (sqlTypeDescriptor.getSqlType() == java.sql.Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }
        return super.remapSqlTypeDescriptor(sqlTypeDescriptor);
    }
}