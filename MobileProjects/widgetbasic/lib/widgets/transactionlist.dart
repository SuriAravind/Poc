import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../models/transaction.dart';

class TransactionList extends StatelessWidget {

  final List<Transaction> transaction;

  TransactionList(this.transaction);

  //Transaction(id: '22', title: 'B1', amount: 599, dateTime: DateTime.now()),
  //Transaction(id: '33', title: 'C1', amount: 899, dateTime: DateTime.now()),];

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 400,
      child: SingleChildScrollView(
        child: Column(
          children: transaction.map((transaction) {
            return Card(
              color: Colors.lightBlue,
              child: Row(
                children: <Widget>[
                  Container(
                    child: Text(
                      //transaction.amount.toString(),
                      '\$${transaction.amount}',
                      style: TextStyle(color: Colors.white),
                    ),
                    margin: EdgeInsets.symmetric(horizontal: 20, vertical: 20),
                    decoration: BoxDecoration(
                        border: Border.all(color: Colors.blue, width: 5)),
                    padding: EdgeInsets.all(10),
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        'Id :' + transaction.title,
                        style: TextStyle(
                            color: Colors.white, fontWeight: FontWeight.bold),
                      ),
                      Text(
                        'Start Date :' +
                            DateFormat.yMMMd()
                                .format(transaction.dateTime)
                                .toString(),
                        style: TextStyle(
                            color: Colors.white, fontWeight: FontWeight.bold),
                      ),
                    ],
                  )
                ],
              ),
            );
          }).toList(),
        ),
      ),
    );
  }
}
