from transformers import pipeline
model = pipeline(model = "deepset/roberta-base-squad2-distilled")

model(question="Which border does the Rhine flow from the south?", context="Lake Constance consists of three bodies of water: the Obersee (\"upper lake\"), the Untersee (\"lower lake\"), and a connecting stretch of the Rhine, called the Seerhein (\"Lake Rhine\"). The lake is situated in Germany, Switzerland and Austria near the Alps. Specifically, its shorelines lie in the German states of Bavaria and Baden-Württemberg, the Austrian state of Vorarlberg, and the Swiss cantons of Thurgau and St. Gallen. The Rhine flows into it from the south following the Swiss-Austrian border. It is located at approximately 47°39′N 9°19′E / 47.650°N 9.317°E / 47.650; 9.317.")

#######

model = pipeline(model = "sysresearch101/t5-large-finetuned-xsum-cnn")

article = "Capt Ranong Chumpinit told the BBC that Daniel Clarke was found at 01:05 GMT on Saturday lying by the train track in Thung-Kha, Chumphon province. He said Mr Clarke, from Aldershot, told police that he stepped out to smoke between two carriages when he fell. The Foreign Office said a Briton had been hospitalised in Thailand. \"We are supporting the family of a British national who has been hospitalised in Thailand,\" a spokeswoman said. Capt Ranong said a friend of the backpacker told police it was an accident. He said: \"We don't believe there's a foul play going on because his belongings remained intact.\""
print(model(article)[0]['summary_text'])
