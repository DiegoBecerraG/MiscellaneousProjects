#include "TrieNode.h"
#include <string>

TrieNode::TrieNode(char c) {
    //Assigns a default char value
	character = c;

}

bool TrieNode::hasChildren() {
    //Checks to see if any of the nodes have children, returns
    //true if so
	for(int i = 0; i < CHAR_SIZE - 1; i++) {
		if(next[i] != nullptr && !isLeaf()) {
			return true;
		}
	}
	return false;
}

void TrieNode::setAsLeaf() {
    //Assigns the '$' to the final node in the TrieNode
	struct TrieNode *leaf = new TrieNode('$');
	next[CHAR_SIZE - 1] = leaf;
}

void TrieNode::setAsInteriorNode() {
  //Removes the '$'
	next[CHAR_SIZE - 1] = nullptr;
}

bool TrieNode::isLeaf() {
    //Checks to see if there is a '$' present
	if(next[26] != 0) {
		return true;
	} else {
		return false;
	}
}
