#ifndef _TRIE_H_
#define _TRIE_H_

#include "TrieNode.h"
#include <iostream>
#include <string>
using namespace std;

class Trie {
public:
  struct TrieNode *newNode;

  Trie();

  // Returns true if the trie is empty, false otherwise
  bool isEmpty();

  // Inserts the word into the trie, and returns the status
  // (e.g. fail the operation if the word has any non-alphabetical
  // letter)
  bool insert(string word);

  // Removes the word from the trie, and returns the status
  bool remove(struct TrieNode* node, string word, int d);

  // Returns true if the word is present in the trie, false otherwise
  bool search(string word);

  // Displays all the words currently in the trie
  void displayWordList(struct TrieNode* node, char str[], int level);

private:
  //
};
#endif // _TRIE_H_
