package com.example.crud

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "ProductManager.db" // Nombre de la base de datos
        const val DATABASE_VERSION = 1 // Versión de la base de datos

        // Nombre de la tabla y columnas
        const val TABLE_NAME = "products" // Nombre de la tabla
        const val COLUMN_ID = "id" // Columna para el ID único de cada producto
        const val COLUMN_NAME = "name" // Columna para el nombre del producto
        const val COLUMN_PRICE = "price" // Columna para el precio del producto
        const val COLUMN_QUANTITY = "quantity" // Columna para la cantidad del producto

    }

    override fun onCreate(db: SQLiteDatabase) {
        // Instrucción SQL para crear la tabla
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PRICE REAL NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL
            )
        """
        db.execSQL(createTableQuery) // Ejecuta la instrucción para crear la tabla
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente si ya existe y la recrea
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertProduct(name: String, price: Double, quantity: Int): Long {
        val db = writableDatabase // Obtiene la base de datos en modo escritura
        val values = ContentValues().apply {
            put(COLUMN_NAME, name) // Añade el nombre del producto
            put(COLUMN_PRICE, price) // Añade el precio del producto
            put(COLUMN_QUANTITY, quantity) // Añade la cantidad del producto
        }
        return db.insert(TABLE_NAME, null, values) // Inserta los datos y retorna el ID del nuevo registro
    }



    fun updateProduct(id: Int, name: String, price: Double, quantity: Int): Int {
        val db = writableDatabase // Obtiene la base de datos en modo escritura
        val values = ContentValues().apply {
            put(COLUMN_NAME, name) // Actualiza el nombre
            put(COLUMN_PRICE, price) // Actualiza el precio
            put(COLUMN_QUANTITY, quantity) // Actualiza la cantidad
        }
        // Actualiza el registro donde el ID coincide
        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun deleteProduct(id: Int): Int {
        val db = writableDatabase // Obtiene la base de datos en modo escritura
        // Elimina el registro donde el ID coincide
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
    fun getAllProducts(): List<Product> {
        val db = readableDatabase
        val productList = mutableListOf<Product>()
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_NAME ASC")
        cursor.use {
            while (it.moveToNext()) {
                val product = Product(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE)),
                    quantity = it.getInt(it.getColumnIndexOrThrow(COLUMN_QUANTITY))
                )
                productList.add(product)
            }
        }
        return productList
    }
}
