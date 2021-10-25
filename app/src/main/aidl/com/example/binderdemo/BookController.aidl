// BookController.aidl
package com.example.binderdemo;

import com.example.binderdemo.Book;

// Declare any non-default types here with import statements

interface BookController {

   List<Book> getBookList();

   void addBook(in Book book);

   Book getBook(in int pos);
}