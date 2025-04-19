#include "Trie.h"


Trie::Trie() {
  //Creates an "empty" TrieNode
	newNode = new TrieNode('0');
}

bool Trie::isEmpty() {
	bool empty = true;
    
  //Checks to see if the entire node is empty
	for(int i = 0; i < 27; i++)
	{
		if(newNode->next[i] != nullptr)
		{
			empty = false;
		}
	}

	return empty;
}

bool Trie::insert(string word) {
	struct TrieNode *current = newNode;
	int pos = 0;
	char charac = 'a';
    
  //Inserts the word to the TrieNode
	for(int i = 0; i < word.length(); i++) {
    charac = word.at(i);

    //Makes sure the word only has alpha characters
		if(!isalpha(charac)) {
			return false;
		}

		pos = charac - 'a';

		if(current->next[pos] == 0) {
			struct TrieNode *nNode = new TrieNode(charac);
			current->next[pos] = nNode;
		}
    
		current = current->next[pos];
	}

	current->setAsLeaf();
	return true;
}


bool Trie::remove(struct TrieNode* node, string word, int d) {
	if(isEmpty()) {
		return false;
	}
	
	//Removes the word from the TrieNode
	if(d == word.size()) {
	  //Removes the '$'
		if(node->isLeaf()) {
			node->setAsInteriorNode();
		}
		
		if(isEmpty()) {
			delete (node);
			node = nullptr;
		}
    
		return node;
	}
	
	int character = word[d] - 'a';
	remove(node->next[character], word, d + 1);
	if(isEmpty() && node->isLeaf() == false) {
		delete (node);
		node = nullptr;
	}
	return newNode;
}

bool Trie::search(string word) {
	if(isEmpty()) {
		return false;
	}
    
  //Assigns variables needed
  char letter = 'a';
  int pos = 0;
	struct TrieNode *node = newNode;

  //Goes through the node and checks is the selected
  //character is present
	for(int i = 0; i < word.length(); i++) {
		letter = word.at(i);
		pos = (int)letter - 97;
		if(node->next[pos] == nullptr) {
			return false;
		}
		node = node->next[pos];
	}
	
	if(node->next[26] == 0) {
	    return false;
	} else {
      return true;   
  }
}

void Trie::displayWordList(struct TrieNode* node, char chr[], int depth) {
  //Prints out the entire tree the way it was inserted
	if(node->isLeaf()) {
		chr[depth] = '\0';
		cout << chr << endl;
	}

	for(int i = 0; i < 26; i++) {
    if(node->next[i] != nullptr) {
		  chr[depth] = node->next[i]->character;
			displayWordList(node->next[i], chr, depth + 1);
    }
	}
}
