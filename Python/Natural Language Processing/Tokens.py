!python -m spacy download en_core_web_sm
import spacy

model = spacy.load("en_core_web_sm")
doc = model("I think John to like his beer.")
for token in doc:
  print(token.text, token.pos_)

##################################
from transformers import pipeline
model_name = "QCRI/bert-base-multilingual-cased-pos-english"
model = pipeline("token-classification", model=model_name)

doc = model("All clouds are made up of basically the same thing: water droplets or different ice crystal that float in the sky. But all clouds look a little bit different from one another, and sometimes these differences can help us predict a change in the weather.")
mult = 1
for token in doc:
  print(token['word'], token['entity'], token['score'])
  mult = mult * token['score']
mult
