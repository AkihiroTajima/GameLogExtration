import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import xgboost as xgb
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from keras.utils import np_utils
from sklearn.model_selection import train_test_split
from keras.utils import np_utils

from sklearn.model_selection import train_test_split
from keras.utils import np_utils

df = pd.read_csv('/home/kenzi/pg/AIWolf/GameLogExtration/InputTalkData4.csv', dtype=str)
df = df.astype(str)
print(df)

X_train, X_test, Y_train, Y_test = train_test_split(
    df[['text']], np_utils.to_categorical(df[['Objective']]), 
    test_size=0.2, random_state=0
)

print(X_train.shape, X_test.shape, Y_train.shape, Y_test.shape)
print(type(X_train))

from keras.preprocessing.text import Tokenizer
from keras.preprocessing.sequence import pad_sequences

max_len = 300  # 1メッセージの最大単語数 (不足分はパディング)

tokenizer = Tokenizer()
tokenizer.fit_on_texts(X_train['text'])
x_train = tokenizer.texts_to_sequences(X_train['text'])
x_test = tokenizer.texts_to_sequences(X_test['text'])

x_train = pad_sequences(x_train, maxlen=max_len)
x_test = pad_sequences(x_test, maxlen=max_len)

from keras.models import Sequential
from keras.layers import LSTM, Dense, Embedding

vocabulary_size = len(tokenizer.word_index) + 1  # 学習データの語彙数+1

model = Sequential()

model.add(Embedding(input_dim=vocabulary_size, output_dim=32))
model.add(LSTM(16, return_sequences=False))
model.add(Dense(Y_test.shape[1], activation='softmax'))

model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

model.summary()

# テストデータの設定
y_train = Y_train
y_test = Y_test

# 学習
history = model.fit(
    x_train, y_train, batch_size=32, epochs=10,
    validation_data=(x_test, y_test)
)

from sklearn.metrics import confusion_matrix

y_pred = model.predict_classes(x_test)
rounded_labels=np.argmax(y_test, axis=1)
print(confusion_matrix(rounded_labels, y_pred))

print(y_test[1])
rounded_labels=np.argmax(y_test, axis=1)
print(rounded_labels[1])

model.save('model5.h5')

#正答率
plt.plot(history.history['accuracy'])
plt.plot(history.history['val_accuracy'])
plt.title('model accuracy')
plt.ylabel('accuracy')
plt.xlabel('epoch')
plt.legend(['train', 'val'], loc='upper right')
plt.show()
#loss
plt.plot(history.history['loss'])
plt.plot(history.history['val_loss'])
plt.title('model loss')
plt.ylabel('loss')
plt.xlabel('epoch')
plt.legend(['train', 'val'], loc='upper right')
plt.show()

from sklearn.metrics import classification_report, confusion_matrix
print(confusion_matrix(rounded_labels, y_pred))
print(classification_report(rounded_labels, y_pred))