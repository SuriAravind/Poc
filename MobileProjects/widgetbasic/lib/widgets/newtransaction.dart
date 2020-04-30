import 'package:flutter/material.dart';

class NewTransaction extends StatelessWidget {
  final Function newTransaction;
  final titleController = TextEditingController();
  final amountController = TextEditingController();

  NewTransaction(this.newTransaction);

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 5,
      child: Container(
        padding: EdgeInsets.all(10),
        child: Column(
          children: <Widget>[
            TextField(
              decoration: InputDecoration(labelText: 'Title'),
              controller: titleController,
              autofocus: true,
            ),
            TextField(
              decoration: InputDecoration(labelText: 'Amount'),
              autofocus: true,
              controller: amountController,
            ),
            FlatButton(
                onPressed: () {
                  newTransaction(titleController.text,
                      double.parse(amountController.text));
                },
                textColor: Colors.blue,
                child: Text(
                  'Add Transaction',
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
                padding: EdgeInsets.all(10)),
          ],
        ),
      ),
    );
  }
}
