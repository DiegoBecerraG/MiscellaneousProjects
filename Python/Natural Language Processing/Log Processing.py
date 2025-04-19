!pip install nltk
!pip show nltk
import nltk
from nltk.tokenize import wordpunct_tokenize
from nltk.lm.preprocessing import padded_everygram_pipeline
from nltk.lm import MLE
from nltk.lm import Laplace
from nltk.util import ngrams


from google.colab import drive
drive.mount('/content/drive')

sentences = []

filepath = '/content/drive/MyDrive/Colab_Notebooks/corpus_for_language_models.txt'
f = open(filepath, 'r')
for line in f:
  s = line.strip()
  sentence = wordpunct_tokenize(s)
  sentences.append(sentence)

train, vocab = padded_everygram_pipeline(3, sentences)
lm = MLE(3)
lm.fit(train, vocab)

train, vocab = padded_everygram_pipeline(3, sentences)
lsm = Laplace(3)
lsm.fit(train, vocab)

s1 = "Mervin Lung remains chairman and chief executive officer ."
s2 = "It has n\'t made merger overtures to the board ."

tokens1 = wordpunct_tokenize(s1)
tokens2 = wordpunct_tokenize(s2)

lp1 = 0
ls1 = 0
# Loop Through sentence 1
for i in range(len(tokens1) - 3):
  lp1 = lp1 + lm.logscore(tokens1[i+2], tokens1[i:i+2])
  ls1 = ls1 + lsm.logscore(tokens1[i+2], tokens1[i:i+2])


lp2 = 0
ls2 = 0
# Loop through sentence 2
for j in range(len(tokens2) - 3):
  lp2 = lp2 + lm.logscore(tokens2[j+2], tokens2[j:j+2])
  ls2 = ls2 + lsm.logscore(tokens2[j+2], tokens2[j:j+2])


# Log Probability

print('Log Probability for sentence 1: ')
print(lp1)
print('Log Probability for sentence 2: ')
print(lp2)


# Laplace Smoothing

print('Laplace Smoothing for sentence 1: ')
print(ls1)
print('Laplace Smoothing for sentence 2: ')
print(ls2)
