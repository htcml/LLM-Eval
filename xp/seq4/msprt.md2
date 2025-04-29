
## Mixture Sequential Probability Ratio Test (mSPRT)

Mixture Sequential Probability Ratio Test(mSPRT) is one of the sequential testing algorithms used in One XP. The sequential testing algorithms enable users to monitor test results continuously and safely conclude a test whenever a significant effect is present. Below a brief introduction of mSPRT is given.

In an A/B testing setting, assume the control variables {$X^{(i)}_{ctrl}$} are independent random variables from a distribution with a density function $f(x|\mu_{ctrl},\sigma_{ctrl})$ where $\mu_{ctrl}$ and $\sigma_{ctrl}$ represent mean and standard deviation. Similarly, the distribution density function for the treatment variables {$Y^{(i)}_{trt}$} is $f(y|\mu_{trt},\sigma_{trt})$. The hypothesis to be tested is on the difference in the distribution mean:

$$H_{0}: \theta = \mu_{trt} - \mu_{ctrl} = \theta_{0}$$
$$H_{a}: \theta = \mu_{trt} - \mu_{ctrl} \neq \theta_{0}$$


Assume $\theta$ takes values in $\Theta \subset R$. The test statistic used in mSPRT is a likelihood ratio integrated over a a mixing distribution $H$, where $H$ is assumed to have a density $h(\theta)$ that is positive everywhere  over $\Theta$:

$$\Lambda^{H, \theta_{0}}_{n} = \int_{\Theta} \prod^{n}_{m=1} \frac{f_{\theta}(X_{m})}{f_{\theta_{0}}(X_{m})} h(\theta) d\theta $$

Given a desired false positive probability $\alpha$, it stops and rejects the null hypothesis at **the first time** $T = T^{H}(\alpha)$ that $\Lambda^{H, \theta_{0}}_{n} > \alpha^{-1}$; if no such time exists, it never rejects the null hypothesis. **Using standard martingale techniques, it can be shown that this sequential test controls Type I error at level $\alpha$**.

For data generated from general exponential families, as long as an appropriate conjugate distribution is chosen as $H$, computation of $\Lambda^{H, \theta_{0}}_{n}$ is inexpensive. For example, for data generated from a normal distribution $N(\mu, \sigma^{2})$, it turns out that if we use a normal mixing distribution centered at the null hypothesis(i.e., $H = N(\theta_{0}, \tau)$), then we can obtain a **closed form formula** for $\Lambda^{H,\theta_{0}}_{n}$:

$$\Lambda^{H,\theta_{0}}_{n} = \sqrt{\frac{V_{n}}{V_{n}+\tau}} \times exp \{ \frac{\tau }{2V_{n}(V_{n}+\tau)}(\overline{Y}_{n} - \overline{X}_{n} - \theta_{0})^{2}\}\ \ \ \ (eq.\ 1)$$ 

, where $V_{n}$ is sample variane of $\overline{Y}_{n} - \overline{X}_{n}$.
    
      
The $\tau$ parameter in $H$ is the only hyperparameter needed to be tuned. Currently we set $\tau$ = 0.0001 in the program.

[1]  
     
     
Similar to p-value and confidence interval employed in classical hypothesis testing, the sequential version of p-value and confidence interval can be defined for mSPRT as follows:

## Always valid p-value process

Definition:
>A sequence of p-values ($p_{n}$) is an always valid p-value process if given any stopping time $T$, there holds:  
>  
>$$\forall \alpha \in [0, 1], P_{\theta_{0}}(p_{T} \le \alpha) \le \alpha $$


For a given mSPRT $\Lambda^{H, \theta_{0}}_{n}$ sequence, it can be shown that $p_{n}$ satisfying the following simple recursion is an always valid p-value process:  
  
$$p_{0} = 1; p_{n} = min \{ p_{n-1}, 1/{\Lambda^{H, \theta_{0}}_{n}}\}$$ [1]

## Always valid confidence interval process

Definition:

>A sequence of (1- $\alpha$)-level confidence intervals ($I_{n}$) is an always valid confidence interval process if for any stopping time $T$, the corresponding interval $I_{T}$ has $1 - \alpha$ coverage of the true parameter: 
>  
>$$\forall \theta \in \Theta, P_{\theta}(\theta \in I_{T}) \ge 1- \alpha $$


For a given mSPRT $\Lambda^{H, \theta_{0}}_{n}$ sequence, it also can be shown that $I_{n}$ satisfying the following simple recursion is an always valid confidence interval process:  

$$I_{0} = \Theta; I_{n} = I_{n-1} \cap \{\theta: \Lambda^{H, \theta}_{n} \ge \alpha^{-1}\}$$ 


Using the $eq. 1$, we can derive the range of $\theta$ that satisifies $\{\theta: \Lambda^{H, \theta}_{n} \ge \alpha^{-1}\}$, i.e.


$$\overline{Y}_{n} - \overline{X}_{n} \pm \sqrt{\frac{V_{n}(V_{n}+\tau)}{\tau}(-2\log(\alpha)-\log(\frac{V_{n}}{V_{n}+\tau})}$$

[1]

## Sample size estimation

Let $N_{\delta}$ denote the sample size of power 1 for given effect size $\delta$. The following formula provides a reasonable approximation for the average sample size of power 1.  
<br>
$E[N_{\delta}] = \frac{v_{x}+v_{y}}{\delta^{2}}\{log(-2 \tau e^{\delta^{2}/\tau} \log(\alpha)) - log(\delta^{2} \alpha^{2}) - 1\}$, where $\delta$ is the effect size.
<br>
<br>
Let $N_{\delta_{1-\beta}}$ denote the sample size threshold for achieving the power $1 - \beta$ for given $\delta$. The following formula provides a reasonable approximation.
  
$N_{\delta_{1-\beta}} = (0.35 - 0.79\log(\beta)) E[N_{\delta}]$.

Given this formula, the power can be estimated by solving the formula for $\beta$ for a given sampel size $N_{\delta_{1-\beta}}$.

[2]

## References:
    
[1] R. Johari, P. Koomen, L. Pekelis, and D. Walsh, "Peeking at a/b tests: Why it matters, and what to do about it," in Proceedings of the 23rd ACM SIGKDD International Conference
on Knowledge Discovery and Data Mining. ACM, 2017, pp. 1517–1525.

[2] Z. Zhao, M. Liu, and A. Deb, "Safely and Quickly Deploying New Features with a Staged Rollout Framework Using Sequential Test and Adaptive Experimental Design," in 3rd International Conference on Computational Intelligence and Applications (ICCIA). IEEE, 2018, pp. 59–70.
