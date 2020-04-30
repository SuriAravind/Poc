import 'package:flutter/material.dart';
import 'package:widgetbasic/models/transaction.dart';
import 'package:widgetbasic/widgets/newtransaction.dart';
import 'package:widgetbasic/widgets/transactionlist.dart';

class UserTranssaction extends StatefulWidget {
  @override
  _UserTranssactionState createState() => _UserTranssactionState();
}

class _UserTranssactionState extends State<UserTranssaction> {
  final List<Transaction> _userTransactionList = [
    Transaction(id: '22', title: 'B1', amount: 599.0, dateTime: DateTime.now()),
    Transaction(id: '33', title: 'C1', amount: 899.0, dateTime: DateTime.now()),
    Transaction(id: '22', title: 'B1', amount: 599.0, dateTime: DateTime.now()),
    Transaction(id: '33', title: 'C1', amount: 899.0, dateTime: DateTime.now()),
    Transaction(id: '22', title: 'B1', amount: 599.0, dateTime: DateTime.now()),
    Transaction(id: '33', title: 'C1', amount: 899.0, dateTime: DateTime.now())
  ];

  void _addTransaction(String txtitle, double txamount) {
    final newTx = Transaction(
      id: DateTime.now().toString(),
      title: txtitle,
      amount: txamount,
      dateTime: DateTime.now(),
    );
    setState(() {
      _userTransactionList.add(newTx);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        NewTransaction(_addTransaction),
        TransactionList(_userTransactionList)
      ],
    );
  }
}
