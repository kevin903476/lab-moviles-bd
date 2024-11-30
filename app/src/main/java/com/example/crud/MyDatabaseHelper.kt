package com.example.crud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS) {

    companion object {
        const val NOMBRE_BASE_DATOS = "tarea.db"
        const val VERSION_BASE_DATOS = 1

        // Tabla Producto
        const val TABLA_PRODUCTO = "producto"
        const val COLUMNA_ID_PRODUCTO = "idProducto"
        const val COLUMNA_NOMBRE_PRODUCTO = "nombre"
        const val COLUMNA_PRECIO_PRODUCTO = "precio"
        const val COLUMNA_CANTIDAD_PRODUCTO = "cantidad"
        const val COLUMNA_ID_PROVEEDOR_PRODUCTO = "idProveedor"

        // Tabla Proveedor
        const val TABLA_PROVEEDOR = "proveedor"
        const val COLUMNA_ID_PROVEEDOR = "idProveedor"
        const val COLUMNA_NOMBRE_PROVEEDOR = "nombre"
        const val COLUMNA_EMPRESA_PROVEEDOR = "empresa"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla proveedor
        val crearTablaProveedor = """
            CREATE TABLE $TABLA_PROVEEDOR (
                $COLUMNA_ID_PROVEEDOR INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMNA_NOMBRE_PROVEEDOR TEXT NOT NULL,
                $COLUMNA_EMPRESA_PROVEEDOR TEXT NOT NULL
            )
        """
        db.execSQL(crearTablaProveedor)

        // Crear tabla producto
        val crearTablaProducto = """
            CREATE TABLE $TABLA_PRODUCTO (
                $COLUMNA_ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMNA_NOMBRE_PRODUCTO TEXT NOT NULL,
                $COLUMNA_PRECIO_PRODUCTO REAL NOT NULL,
                $COLUMNA_CANTIDAD_PRODUCTO INTEGER NOT NULL,
                $COLUMNA_ID_PROVEEDOR_PRODUCTO INTEGER,
                FOREIGN KEY ($COLUMNA_ID_PROVEEDOR_PRODUCTO) REFERENCES $TABLA_PROVEEDOR($COLUMNA_ID_PROVEEDOR)
            )
        """
        db.execSQL(crearTablaProducto)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PRODUCTO")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_PROVEEDOR")
        onCreate(db)
    }

    // Método para verificar si existe un proveedor
    fun existeProveedor(idProveedor: Int): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLA_PROVEEDOR,
            arrayOf(COLUMNA_ID_PROVEEDOR),
            "$COLUMNA_ID_PROVEEDOR = ?",
            arrayOf(idProveedor.toString()),
            null, null, null
        )
        cursor.use {
            return it.count > 0 // Devuelve true si hay al menos un proveedor con el ID dado
        }
    }

    // Métodos para la tabla producto
    fun insertarProducto(nombre: String, precio: Double, cantidad: Int, idProveedor: Int?): Long {
        if (idProveedor != null && !existeProveedor(idProveedor)) {
            throw IllegalArgumentException("El proveedor con ID $idProveedor no existe.")
        }

        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COLUMNA_NOMBRE_PRODUCTO, nombre)
            put(COLUMNA_PRECIO_PRODUCTO, precio)
            put(COLUMNA_CANTIDAD_PRODUCTO, cantidad)
            put(COLUMNA_ID_PROVEEDOR_PRODUCTO, idProveedor)
        }
        return db.insert(TABLA_PRODUCTO, null, valores)
    }

    fun actualizarProducto(id: Int, nombre: String, precio: Double, cantidad: Int, idProveedor: Int?): Int {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COLUMNA_NOMBRE_PRODUCTO, nombre)
            put(COLUMNA_PRECIO_PRODUCTO, precio)
            put(COLUMNA_CANTIDAD_PRODUCTO, cantidad)
            put(COLUMNA_ID_PROVEEDOR_PRODUCTO, idProveedor)
        }
        return db.update(TABLA_PRODUCTO, valores, "$COLUMNA_ID_PRODUCTO=?", arrayOf(id.toString()))
    }

    fun eliminarProducto(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLA_PRODUCTO, "$COLUMNA_ID_PRODUCTO=?", arrayOf(id.toString()))
    }

    fun obtenerProductos(): List<Producto> {
        val db = readableDatabase
        val listaProductos = mutableListOf<Producto>()
        val cursor = db.query(TABLA_PRODUCTO, null, null, null, null, null, "$COLUMNA_NOMBRE_PRODUCTO ASC")
        cursor.use {
            while (it.moveToNext()) {
                val producto = Producto(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMNA_ID_PRODUCTO)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMNA_NOMBRE_PRODUCTO)),
                    precio = it.getDouble(it.getColumnIndexOrThrow(COLUMNA_PRECIO_PRODUCTO)),
                    cantidad = it.getInt(it.getColumnIndexOrThrow(COLUMNA_CANTIDAD_PRODUCTO)),
                    idProveedor = it.getInt(it.getColumnIndexOrThrow(COLUMNA_ID_PROVEEDOR_PRODUCTO))
                )
                listaProductos.add(producto)
            }
        }
        return listaProductos
    }

    // Métodos para la tabla proveedor
    fun insertarProveedor(nombre: String, empresa: String): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COLUMNA_NOMBRE_PROVEEDOR, nombre)
            put(COLUMNA_EMPRESA_PROVEEDOR, empresa)
        }
        return db.insert(TABLA_PROVEEDOR, null, valores)
    }

    fun actualizarProveedor(id: Int, nombre: String, empresa: String): Int {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put(COLUMNA_NOMBRE_PROVEEDOR, nombre)
            put(COLUMNA_EMPRESA_PROVEEDOR, empresa)
        }
        return db.update(TABLA_PROVEEDOR, valores, "$COLUMNA_ID_PROVEEDOR=?", arrayOf(id.toString()))
    }

    fun eliminarProveedor(id: Int): Int {
        val db = writableDatabase
        val cursor = db.query(
            TABLA_PRODUCTO,
            arrayOf(COLUMNA_ID_PRODUCTO),
            "$COLUMNA_ID_PROVEEDOR_PRODUCTO = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        cursor.use {
            if (it.count > 0) {
                return 0 // No se puede eliminar porque tiene productos asociados
            }
        }
        return db.delete(TABLA_PROVEEDOR, "$COLUMNA_ID_PROVEEDOR=?", arrayOf(id.toString()))
    }

    fun obtenerProveedores(): List<Proveedor> { 
        val db = readableDatabase
        val listaProveedores = mutableListOf<Proveedor>()
        val cursor = db.query(TABLA_PROVEEDOR, null, null, null, null, null, "$COLUMNA_NOMBRE_PROVEEDOR ASC")
        cursor.use {
            while (it.moveToNext()) {
                val proveedor = Proveedor(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMNA_ID_PROVEEDOR)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMNA_NOMBRE_PROVEEDOR)),
                    empresa = it.getString(it.getColumnIndexOrThrow(COLUMNA_EMPRESA_PROVEEDOR))
                )
                listaProveedores.add(proveedor)
            }
        }
        return listaProveedores
    }
}
