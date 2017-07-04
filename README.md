Latent Dirichlet allocation - natural language processing
======================
author: Tereza Štanglová

Latent Dirichlet allocation (LDA) is a generative statistical model for collection of discrete data. It is unsupervised learning algorithm.

LDA represents documents as mixture of topics. Each word in document has its own certain probability. Topics can be classified as sport related or law related. A topic has probabilities of generating various words. Words with higher probability define the topic. For example, sport related words are <i>team</i>, <i>ball</i> or <i>game</i>.

Algorithm is iterative. Topic mixture for each document has a Dirichlet distribution over a fixed set of <i>K</i> topics. At the beginning, sets of the most likely words for topic are a little bit messy. But words in these sets make more sense with each iteration.

## Algorithm

1. Set a random topic number (uniform distribution) to each word in each document.

2. Make <i>N</i> iterations of Gibbs sampling to improve all the topics. Iterative algorithm updates just one parameter per time unit.

	1. Topic number of current word cannot be included in the calculation. Therefore the counts of topics (for words and documents also) are decreased.

	2. Two thing have to be compute for each topic.

		a. P(topic t | document d) - the proportion of words in document <i>d</i> that are currently assigned to topic <i>t</i>

		b. P(word w | topic t) - the proportion of assignments to topic <i>t</i> over all documents that come from this word <i>w</i>

		Reassign word <i>w</i> a new topic with probability P = P(topic t | document d) * P(word w | topic t)

		<p align="center">
		  <img src="https://cloud.githubusercontent.com/assets/5311408/14233272/f4d2f26e-f9c3-11e5-98f7-26730f56efd8.gif">
		</p>

	3. New topic number of current word must be increased in all counts.

3. Compute perplexity for model every <i>t</i> step. Perplexity is a measurement of how well a probability model predicts a sample. Therefore it has to converge.  A low perplexity indicates a good model.

### Notes 

Corpus for my program is in czech. File <code>data/train.txt</code>

Classes for reading this file are work of NLP group at Faculty of Applied Sciences, University of West Bohemia in Czech. 
