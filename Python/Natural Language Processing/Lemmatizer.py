!pip install nltk
import nltk
nltk.download('punkt')
nltk.download('wordnet')
nltk.download('stopwords')
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from string import punctuation
from nltk.stem import WordNetLemmatizer

text="Every essay she's written and that I've read is on that pile."
text=text.lower()
tokens=word_tokenize(text)
print("The tokens are: ")
print(tokens)
print(len(tokens))

english_stopwords = stopwords.words("english")
tokens = [token for token in tokens if token not in english_stopwords]

tokens = [token for token in tokens if token not in punctuation]

lemmatizer = WordNetLemmatizer()
lemmas = [lemmatizer.lemmatize(token) for token in tokens]
print("The lemmas are: ")
print(lemmas)
print(len(lemmas))
